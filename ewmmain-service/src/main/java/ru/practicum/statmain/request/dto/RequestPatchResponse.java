package ru.practicum.statmain.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestPatchResponse {

    List<RequestResponse> confirmedRequests;

    List<RequestResponse> rejectedRequests;
}

