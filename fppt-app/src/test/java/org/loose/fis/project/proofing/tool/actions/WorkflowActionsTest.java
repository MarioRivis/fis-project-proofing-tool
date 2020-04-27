package org.loose.fis.project.proofing.tool.actions;

import org.junit.jupiter.api.Test;
import org.loose.fis.project.proofing.tool.StudentResponse;
import org.loose.fis.project.proofing.tool.github.client.dto.request.repository.contents.CreateFileRequestBody;

import java.util.Base64;

class WorkflowActionsTest {


    @Test
    void name() {
        StudentResponse studentResponse = new StudentResponse();
        studentResponse.setBuildTool("Maven");
        CreateFileRequestBody contentBodyFor = WorkflowActions.createContentBodyFor(studentResponse);
        System.out.println(new String(Base64.getDecoder().decode(contentBodyFor.getContent())));
    }
}
