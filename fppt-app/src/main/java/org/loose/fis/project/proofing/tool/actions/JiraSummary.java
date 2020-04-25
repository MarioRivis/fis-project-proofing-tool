package org.loose.fis.project.proofing.tool.actions;

import com.google.api.client.util.Key;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class JiraSummary {

    @Key
    private List<String> stories;
    @Key
    private List<String> unassignedStories;
    @Key
    private List<String> tasks;
    @Key
    private List<String> unassignedTasks;
    @Key
    private List<String> subTasks;
    @Key
    private List<String> unassignedSubTasks;
    @Key
    private Map<String, Integer> storiesToSubtaskNumber;
}
