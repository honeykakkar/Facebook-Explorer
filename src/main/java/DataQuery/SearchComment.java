package DataQuery;

/*
 * Author: Honey Kakkar
 * Created on: February 14, 2017
 * Package: DataQuery
 * Project: Facebook Explorer
 */

import Auth.AuthorizedClient;
import com.restfb.DefaultJsonMapper;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.exception.FacebookGraphException;
import com.restfb.exception.FacebookOAuthException;
import com.restfb.json.JsonObject;
import com.restfb.types.MessageTag;

import java.util.ArrayList;
import java.util.List;


// This class contains methods to search a Facebook comment (or reply) and get the associated data with a given comment (or reply) ID.

public class SearchComment {

    // Object used to represent Facebook client which authorizes the API call to fetch the post
    private final AuthorizedClient authorizedClient;

    // Constructor to obtain the authorization through application access token.
    // As it is necessary to obtain authorization first, the constructor throws exception if it fails to obtain the access token
    public SearchComment(String appID, String appSecret) throws FacebookOAuthException {
        authorizedClient = new AuthorizedClient();
        authorizedClient.appAuthorization(appID, appSecret);
    }

    // Constructor to use an already authorized Facebook client
    public SearchComment(AuthorizedClient authorizedClient) {
        this.authorizedClient = authorizedClient;
    }

    // Method to get the reference of the authorized Facebook client
    public AuthorizedClient getAuthorizedClient() {
        return authorizedClient;
    }

    // Method to get the count of likes on a comment (or reply) with a given comment (or reply) ID
    public Integer getLikesCount(String commentID) {
        // Obtaining the reference of authorized Facebook client required to make the API call
        FacebookClient fbClient = authorizedClient.getFbClient();
        try {
            // Fetches a single Graph API object, mapping the result to an instance of objectType (JsonObject in this case).
            JsonObject commentLikes = fbClient.fetchObject(commentID, JsonObject.class, Parameter.with("fields", "like_count"));
            // Return the count of likes on the comment with the given commentID
            return commentLikes.get("like_count").asInt();
        } catch (FacebookGraphException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // Method to get the count of replies on a comment (or reply) with a given comment (or reply) ID
    public Integer getRepliesCount(String commentID) {
        // Obtaining the reference of authorized Facebook client required to make the API call
        FacebookClient fbClient = authorizedClient.getFbClient();
        try {
            // Fetches a single Graph API object, mapping the result to an instance of objectType (JsonObject in this case).
            JsonObject commentReplies = fbClient.fetchObject(commentID, JsonObject.class, Parameter.with("fields", "comment_count"));
            // Return the count of replies on the comment with the given commentID
            return commentReplies.get("comment_count").asInt();
        } catch (FacebookGraphException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // Method to get the text of a comment (or reply) with a given comment (or reply) ID
    public String getCommentText(String commentID) {
        // Obtaining the reference of authorized Facebook client required to make the API call
        FacebookClient fbClient = authorizedClient.getFbClient();
        try {
            // Fetches a single Graph API object, mapping the result to an instance of objectType (JsonObject in this case).
            JsonObject commentText = fbClient.fetchObject(commentID, JsonObject.class, Parameter.with("fields", "message"));
            // Return the text of the comment with the given commentID
            return commentText.get("message").asString();
        } catch (FacebookGraphException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // Method to get the name of author of a comment (or reply) with a given comment (or reply) ID
    public String getAuthorName(String commentID) {
        // Obtaining the reference of authorized Facebook client required to make the API call
        FacebookClient fbClient = authorizedClient.getFbClient();
        try {
            // Fetches a single Graph API object, mapping the result to an instance of objectType (JsonObject in this case).
            JsonObject commentAuthor = fbClient.fetchObject(commentID, JsonObject.class, Parameter.with("fields", "from"));
            // Return the name of author of the comment with the given commentID
            return commentAuthor.get("from").asObject().get("name").asString();
        } catch (FacebookGraphException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // Method to get the names of users tagged in a comment (or reply) with a given comment (or reply) ID
    public List<String> getUserMentions(String commentID) {
        // Obtaining the reference of authorized Facebook client required to make the API call
        FacebookClient fbClient = authorizedClient.getFbClient();
        try {
            // Fetches a single Graph API object, mapping the result to an instance of objectType (JsonObject in this case).
            JsonObject userMentions = fbClient.fetchObject(commentID, JsonObject.class, Parameter.with("fields", "message_tags"));
            // Creating an instance of JsonMapper for JSON-to-Java mapping
            DefaultJsonMapper mapper = new DefaultJsonMapper();
            // Mapping JSON with mentioned users to a list of MessageTag objects
            List<MessageTag> userTags = mapper.toJavaList(userMentions.get("message_tags").toString(), MessageTag.class);
            // A list to store the names of all users tagged in the comment
            List<String> taggedUserNames = new ArrayList<String>(userTags.size());
            for (MessageTag tag : userTags) {
                // Adding the names of all "user" tags to the list
                if (tag.getType().equals("user"))
                    taggedUserNames.add(tag.getName());
            }
            // Returning the names of users tagged in the comment (or reply)
            return taggedUserNames;
        } catch (FacebookGraphException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Testing the SearchPost class ---\n");
        String appID = "<Enter your application ID here>";
        String appSecret = "<Enter your application secret here>";
        try {
            SearchComment commentSearcher = new SearchComment(appID, appSecret);
            String commentID = "<Enter your comment ID here>";
            System.out.println("Comment author: " + commentSearcher.getAuthorName(commentID));
            System.out.println("Comment text: " + commentSearcher.getCommentText(commentID));
            System.out.println("Total likes on the comment: " + commentSearcher.getLikesCount(commentID));
            System.out.println("Total replies on the comment: " + commentSearcher.getRepliesCount(commentID));
            System.out.println("User mentions in the comment: " + commentSearcher.getUserMentions(commentID));
        } catch (FacebookOAuthException exception) {
            System.out.println(exception.getMessage());
        }
    }
}