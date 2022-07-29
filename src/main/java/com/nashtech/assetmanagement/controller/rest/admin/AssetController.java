package com.nashtech.assetmanagement.controller.rest.admin;

import com.nashtech.assetmanagement.dto.request.RequestCreateAsset;
import com.nashtech.assetmanagement.dto.response.ResponseAssetDTO;
import com.nashtech.assetmanagement.service.AssetService;
import com.nashtech.assetmanagement.service.impl.AssetServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/api/assets")
public class AssetController {


    private final AssetService assetService;
    @Autowired
    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseAssetDTO createAsset(@Valid @RequestBody RequestCreateAsset requestCreateAsset){
        return assetService.createAsset(requestCreateAsset);
    }
}
