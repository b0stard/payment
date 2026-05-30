package com.example.demo.file

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class RenameFileRequest(

    @field:NotBlank
    @field:Size(min = 1, max = 255)
    val newFileName: String
)