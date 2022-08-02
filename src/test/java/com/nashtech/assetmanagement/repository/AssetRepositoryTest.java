package com.nashtech.assetmanagement.repository;


import com.nashtech.assetmanagement.entities.Asset;
import com.nashtech.assetmanagement.repositories.AssetRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Sql(scripts = {"file:src/main/resources/data_test.sql"})
@ActiveProfiles("test")
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AssetRepositoryTest {
    @Autowired
    AssetRepository assetRepository;

    @Test
    void dataLoad() {
        assertEquals(3, assetRepository.findAll().size());
    }

    @Test
    void findAssetByNameOrCodeAndLocationCode_ShouldReturnListAsset_WhenNameAndLocationCodeCorrect() {
        List<Asset> assetListByCode = assetRepository.findAssetByNameOrCodeAndLocationCode("lt", "HCM");
        String patternCode = "(.*[Ll][Tt].*)";
        assertTrue(assetListByCode.get(0).getCode().matches(patternCode)||assetListByCode.get(0).getName().matches(patternCode));
        assertThat(assetListByCode.get(0).getLocation().getCode()).isEqualTo("HCM");
        List<Asset> assetListByName = assetRepository.findAssetByNameOrCodeAndLocationCode("lap", "HCM");
        String patternName = (".*[Ll][Aa][Pp].*");
        assertTrue(assetListByName.get(0).getName().matches(patternName)||assetListByName.get(0).getCode().matches(patternName));
        assertThat(assetListByName.get(0).getLocation().getCode()).isEqualTo("HCM");
    }

}
