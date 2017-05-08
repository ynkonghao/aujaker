package org.konghao.aujaker.dto;

/**
 * Created by 钟述林 393156105@qq.com on 2017/5/5 9:52.
 */
public class MavenDto {

    private String groupId;

    private String artifactId;

    public MavenDto() {
    }

    public MavenDto(String groupId, String artifactId) {
        this.groupId = groupId;
        this.artifactId = artifactId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }
}
