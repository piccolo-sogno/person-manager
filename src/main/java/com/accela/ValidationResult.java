package com.accela;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;

@Data
@Builder
public class ValidationResult {
    @Default private final String message = "";
    @Default private final boolean result = true;
}
