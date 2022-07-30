package com.nashtech.assetmanagement.controller.rest.admin;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.nashtech.assetmanagement.dto.request.RequestCreateAsset;
import com.nashtech.assetmanagement.dto.response.ListAssetResponseDto;
import com.nashtech.assetmanagement.dto.response.ResponseAssetDTO;
import com.nashtech.assetmanagement.service.AssetService;

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

	@GetMapping("/{userId}")
	public ResponseEntity<ListAssetResponseDto> getListAsset(
			@PathVariable("userId") String userId,
			@RequestParam(required = true, name = "page") Integer page,
			@RequestParam(required = true, name = "size") Integer size,
			@RequestParam(required = false, defaultValue = "", value = "keyword") String keyword,
			@RequestParam(required = false, defaultValue = "", value = "categoryId") List<String> categoryId,
			@RequestParam(required = false, defaultValue = "", value = "state") List<String> state) {
		ListAssetResponseDto result = assetService.getListAsset(userId, categoryId, state, keyword, page, size);
		return new ResponseEntity<ListAssetResponseDto>(result, HttpStatus.OK);
	}
	
}
