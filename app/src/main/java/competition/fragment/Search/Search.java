package competition.fragment.Search;

import java.util.ArrayList;
import java.util.List;

public class Search {
    private String publisherAvatarUrl;
    private int publisherId;
    private String publisherUsername;
    private List<String> picturesUrl;
    private String content;
    private String publishTime;
    private String topic;

    public Search(String publisherAvatarUrl,int publisherId,String publisherUsername,List<String> picturesUrl,String content,String publishTime,String topic){
        this.publisherAvatarUrl=publisherAvatarUrl;
        this.publisherId=publisherId;
        this.publisherUsername=publisherUsername;
        this.picturesUrl=picturesUrl;
        this.content=content;
        this.publishTime=publishTime;
        this.topic=topic;
    }

    public void setPublisherAvatarUrl(String publisherAvatarUrl) {
        this.publisherAvatarUrl = publisherAvatarUrl;
    }

    public void setPublisherId(int publisherId) {
        this.publisherId = publisherId;
    }

    public void setPublisherUsername(String publisherUsername) {
        this.publisherUsername = publisherUsername;
    }

    public void setPicturesUrl(List<String> picturesUrl) {
        this.picturesUrl = picturesUrl;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getPublisherAvatarUrl() {
        return publisherAvatarUrl;
    }

    public int getPublisherId() {
        return publisherId;
    }

    public String getPublisherUsername() {
        return publisherUsername;
    }

    public List<String> getPicturesUrl() {
        return picturesUrl;
    }

    public String getContent() {
        return content;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public String getTopic() {
        return topic;
    }
}
