package com.nashtech.assetmanagement.controller.rest.admin;

import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.nashtech.assetmanagement.dto.request.EditAssetRequestDto;
import com.nashtech.assetmanagement.dto.request.CreateAssetRequestDto;
import com.nashtech.assetmanagement.dto.response.AssetResponseDto;
import com.nashtech.assetmanagement.dto.response.EditAssetResponseDto;
import com.nashtech.assetmanagement.dto.response.ListAssetResponseDto;
import com.nashtech.assetmanagement.dto.response.ResponseAssetDto;
import com.nashtech.assetmanagement.dto.response.MessageResponse;
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
	public ResponseAssetDto createAsset(@Valid @RequestBody CreateAssetRequestDto requestCreateAsset) {
		return assetService.createAsset(requestCreateAsset);
	}

	@GetMapping("/{userId}")
	public ResponseEntity<ListAssetResponseDto> getListAsset(@PathVariable("userId") String userId,
			@RequestParam(required = true, name = "page") Integer page,
			@RequestParam(required = true, name = "size") Integer size,
			@RequestParam(required = false, defaultValue = "", value = "keyword") String keyword,
			@RequestParam(required = false, defaultValue = "", value = "sortBy") String sortBy,
			@RequestParam(required = false, defaultValue = "" , value = "sortDirection") String sortDirection,
			@RequestParam(required = false, defaultValue = "", value = "categoryId") List<String> categoryId,
			@RequestParam(required = false, defaultValue = "", value = "state") List<String> state) {
		ListAssetResponseDto result = assetService.getListAsset(userId, categoryId, state, keyword, sortBy,
				sortDirection, page, size);
		return new ResponseEntity<ListAssetResponseDto>(result, HttpStatus.OK);
	}

	@GetMapping("/searchAsset/{location}")
	public ResponseEntity<HashMap> searchAssetByCodeOrName(@RequestParam("text") String text,
			@PathVariable("location") String locationCode) {
		HashMap hashMap = new HashMap<>();
		List<AssetResponseDto> result = assetService.getAssetByCodeOrNameAndLocationCode(text, locationCode);
		hashMap.put("list_asset",result);
		hashMap.put("total",result.size());
		return ResponseEntity.ok(hashMap);
	}

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public EditAssetResponseDto editAsset(@Valid @RequestBody EditAssetRequestDto editAssetRequest,
									   @PathVariable("id") String id) {
		return assetService.editAsset(editAssetRequest, id);
	}

//	582 - Delete asset
	@DeleteMapping("/{assetCode}")
	@CrossOrigin(origins = "*")
	public ResponseEntity<?> deleteAssetByAssetCode(@PathVariable("assetCode") String assetCode) {
		MessageResponse responseMessage = assetService.deleteAssetByAssetCode(assetCode);
		return new ResponseEntity<>(responseMessage, responseMessage.getStatus());
	}

}
