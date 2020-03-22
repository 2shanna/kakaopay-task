package com.kakaopay.greentour.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.kakaopay.greentour.domain.Region;
import com.kakaopay.greentour.dto.Documents;
import com.kakaopay.greentour.dto.EcoInformation;
import com.kakaopay.greentour.dto.LocalResponse;
import com.kakaopay.greentour.repository.RegionRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
@ConfigurationProperties(prefix="dapi")
public class RegionService {

    private final Pattern REMOVE_KEYWORD = Pattern.compile("(\\s+)?(등|일대|일원|번지|,|-|사무소|층|\\n)(\\s*)");
    private final Pattern SPLIT_KEYWORD = Pattern.compile("(\\s*)([및|~]+)(\\s*)");
    private final Pattern NUMERIC_KEYWORD = Pattern.compile("[0-9]+");

    private final String API_PATH = "https://dapi.kakao.com/v2/local/search/address.json";
    private final String AUTH_KEY_PREFIX = "KakaoAK ";
    private final String AUTH_KEY = "7NGkDDFYd0zK+R2Yux9S5KuFz09DyRqkTpMCuTQpZ1po7J9AalAt41MsaCTEGXgD";

    @Autowired
    private RegionRepository regionRepository;

    public String parseRegionName(EcoInformation info) {
        String regionName = info.getRegion();
        if (StringUtils.isEmpty(regionName)) {
            return regionName;
        }
        regionName = REMOVE_KEYWORD.matcher(regionName).replaceAll(" ");
        regionName = SPLIT_KEYWORD.matcher(regionName).replaceAll(" ");
        regionName = NUMERIC_KEYWORD.matcher(regionName).replaceAll(" ").trim();

        StringBuilder parsedRegionName = new StringBuilder();
        String[] regionArr = regionName.split(" ");
        for (int i = 0; i < regionArr.length; i++) {
            String region = regionArr[i];
            if (region.length() > 1) {              // don't save region which name of one letter
                if (i != regionArr.length - 1) {
                    parsedRegionName.append(region).append(" ");
                } else {
                    parsedRegionName.append(region);
                }
            }
        }
        return parsedRegionName.toString();
    }

    public Region getLocalAddress(String regionName) {
        Region region = new Region(regionName);
        StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
        pbeEnc.setAlgorithm("PBEWithMD5AndDES");
        pbeEnc.setPassword("test");
        LocalResponse localRes = request(API_PATH,
                "?query=" + URLEncoder.encode(regionName, StandardCharsets.UTF_8),
                AUTH_KEY_PREFIX + pbeEnc.decrypt(AUTH_KEY));

        if ((int) localRes.getMeta().get("total_count") > 0) {
            Documents doc = localRes.getDocuments().get(0);
            if (doc.getAddress() != null && doc.getAddress().size() > 0) {
                HashMap<String, Object> addrMap = doc.getAddress();
                String region1DeptName = String.valueOf(addrMap.get("region_1depth_name"));
                String region2DeptName = String.valueOf(addrMap.get("region_2depth_name"));
                String region3DeptName = String.valueOf(addrMap.get("region_3depth_name"));
                region.setRegion1DepthName(region1DeptName);
                region.setRegion2DepthName(region2DeptName);
                region.setRegion3DepthName(region3DeptName);
            } else if (doc.getRoadAddress() != null && doc.getRoadAddress().size() > 0) {
                HashMap<String, Object> roadAddrMap = doc.getRoadAddress();
                String region1DeptName = String.valueOf(roadAddrMap.get("region_1depth_name"));
                String region2DeptName = String.valueOf(roadAddrMap.get("region_2depth_name"));
                String region3DeptName = String.valueOf(roadAddrMap.get("region_3depth_name"));
                region.setRegion1DepthName(region1DeptName);
                region.setRegion2DepthName(region2DeptName);
                region.setRegion3DepthName(region3DeptName);
            }
        }

        String region1DeptName = region.getRegion1DepthName();
        String region2DeptName = region.getRegion2DepthName();
        String region3DeptName = region.getRegion3DepthName();
        region.setRegion1DepthName(StringUtils.isEmpty(region1DeptName) ? "" : region1DeptName);
        region.setRegion2DepthName(StringUtils.isEmpty(region2DeptName) ? "" : region2DeptName);
        region.setRegion3DepthName(StringUtils.isEmpty(region3DeptName) ? "" : region3DeptName);
        return region;
    }

    private LocalResponse request(final String apiPath, String paramStr, String key) {
        String requestUrl = apiPath + paramStr;

        HttpsURLConnection conn;
        BufferedReader reader = null;
        InputStreamReader isr = null;

        try {
            URL url = new URL(requestUrl);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", key);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("charset", "UTF-8");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200)
                isr = new InputStreamReader(conn.getInputStream());
            else
                isr = new InputStreamReader(conn.getErrorStream());

            reader = new BufferedReader(isr);
            StringBuilder buffer = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            ObjectMapper objMapper = new ObjectMapper();
            objMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            objMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
            return objMapper.readValue(buffer.toString(), LocalResponse.class);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) try {
                reader.close();
            } catch (Exception ignore) {
            }
            if (isr != null) try {
                isr.close();
            } catch (Exception ignore) {
            }
        }

        return null;
    }

    public List<Region> saveAll(List<EcoInformation> ecoInformationList) {

        // get unique region name set
        Set<String> regionNameSet = new HashSet<>();
        ecoInformationList.stream().map(info ->
                Arrays.asList(parseRegionName(info).split(" ")))
                .forEach(regionNameSet::addAll);

        // update region data with kakao local address information
        List<Region> regionList = regionNameSet
                .stream()
                .map(this::getLocalAddress)
                .collect(Collectors.toList());

        // save region data
        regionList.forEach(regionRepository::saveAndFlush);

        // return all region data
        return regionRepository.findAll();
    }

    public List<Region> save(EcoInformation ecoInfo) {

        // get unique region name set
        Set<String> regionNameSet = new HashSet<>();
        Collections.addAll(regionNameSet, parseRegionName(ecoInfo).split(" "));

        // update region data with kakao local address information
        List<Region> regionList = regionNameSet
                .stream()
                .map(this::getLocalAddress)
                .collect(Collectors.toList());

        // filter the region data that already exists and save the new data
        for (Region region : regionList) {
            List<Region> tmpList = regionRepository.
                    findByRegionNameAndRegion1DepthNameAndAndRegion2DepthNameAndRegion3DepthName(
                    region.getRegionName(), region.getRegion1DepthName(),
                    region.getRegion2DepthName(), region.getRegion3DepthName());
            if (tmpList.isEmpty()) {
                regionRepository.saveAndFlush(region);
            }
        }

        return regionList;
    }
}
