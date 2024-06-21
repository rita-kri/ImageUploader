package com.imageupload.helpers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomResponse {
    private String message;
    private boolean success;
    private Object data;  // This can be any type, adjust according to your needs


}
