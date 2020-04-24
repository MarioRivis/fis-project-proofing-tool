package org.loose.fis.project.proofing.tool;

import com.google.api.client.util.Key;
import lombok.Data;

import java.util.List;

@Data
public class StudentResponseSheet {
    @Key(Config.FORM_NAME)
    private List<StudentResponse> responses;
}
