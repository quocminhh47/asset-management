package com.nashtech.assetmanagement.dto.response;

public interface IAssetReportResponseDto {

    String getName();
    Integer getTotal();
    Integer getAvailable();
    Integer getNotAvailable();
    Integer getRecycled();
    Integer getAssigned();
    Integer getWaitingForRecycled();
}
