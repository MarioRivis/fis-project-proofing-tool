package org.loose.fis.project.proofing.tool.github.client.repository.refs;

import org.junit.jupiter.api.Test;
import org.loose.fis.project.proofing.tool.github.client.dto.response.repository.refs.Ref;
import org.loose.fis.project.proofing.tool.utils.TestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GithubRefsServiceIT {

    private GithubRefsService service = new GithubRefsService("gogmaster", "SimpleRegistrationExample", TestUtils.getGithubCredentials());

    @Test
    void getRef() {
        Ref ref = service.getRef("heads/master");
        assertEquals("refs/heads/master", ref.getRef());
        assertEquals("commit", ref.getObject().getType());
        assertNotNull(ref.getObject().getSha());
    }

    @Test
    void createRef() {
        Ref master = service.getBranch("master");
        Ref actions = service.createRef("refs/heads/actions", master.getObject().getSha());
        assertEquals("refs/heads/actions", actions.getRef());
        assertEquals("commit", actions.getObject().getType());
        assertEquals(master.getObject().getSha(), actions.getObject().getSha());
    }
}