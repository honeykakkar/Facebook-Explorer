package DataQuery;

/*
 * Author: Honey Kakkar
 * Created on: February 15, 2017
 * Package: DataQuery
 * Project: Facebook Explorer
 */

import Auth.AuthorizedClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.exception.FacebookGraphException;
import com.restfb.exception.FacebookOAuthException;
import com.restfb.types.Post;

// This class contains methods to search a Facebook post and get the associated data with a given post ID.

public class SearchPost {

    // Object used to represent Facebook client which authorizes the API call to fetch the post
    private final AuthorizedClient authorizedClient;

    // Constructor to obtain the authorization through application access token.
    // As it is necessary to obtain authorization first, the constructor throws exception if it fails to obtain the access token
    public SearchPost(String appID, String appSecret) throws FacebookOAuthException {
        authorizedClient = new AuthorizedClient();
        authorizedClient.appAuthorization(appID, appSecret);
    }

    // Constructor to use an already authorized Facebook client
    public SearchPost(AuthorizedClient authorizedClient) {
        this.authorizedClient = authorizedClient;
    }

    // Method to get the reference of the authorized Facebook client
    public AuthorizedClient getAuthorizedClient() {
        return authorizedClient;
    }

    // Method to get the count of likes on a post with a given postID
    public Long getLikesCount(String postID) {
        // Obtaining the reference of authorized Facebook client required to make the API call
        FacebookClient fbClient = authorizedClient.getFbClient();
        try {
            // Fetches a single Graph API object, mapping the result to an instance of objectType (Post in this case).
            Post postWithLikes = fbClient.fetchObject(postID, Post.class, Parameter.with("fields", "likes.limit(0).summary(true)"));
            // Return the count of likes on the post with the given postID
            return postWithLikes.getLikesCount();
        } catch (FacebookGraphException exception) {
            System.out.println(exception.getMessage());
            return null;
        }
    }

    // Method to get the count of comments on a post with a given postID
    public Long getCommentsCount(String postID) {
        // Obtaining the reference of authorized Facebook client required to make the API call
        FacebookClient fbClient = authorizedClient.getFbClient();
        try {
            // Fetches a single Graph API object, mapping the result to an instance of objectType (Post in this case).
            Post postWithComments = fbClient.fetchObject(postID, Post.class, Parameter.with("fields", "comments.limit(0).summary(true)"));
            // Return the count of comments on the post with the given postID
            return postWithComments.getCommentsCount();
        } catch (FacebookGraphException exception) {
            System.out.println(exception.getMessage());
            return null;
        }
    }

    // Method to get the count of shares of a post with a given postID
    public Long getSharesCount(String postID) {
        // Obtaining the reference of authorized Facebook client required to make the API call
        FacebookClient fbClient = authorizedClient.getFbClient();
        try {
            // Fetches a single Graph API object, mapping the result to an instance of objectType (Post in this case).
            Post postWithShares = fbClient.fetchObject(postID, Post.class, Parameter.with("fields", "shares.limit(0).summary(true)"));
            // Return the count of shares of the post with the given postID
            return postWithShares.getSharesCount();
        } catch (FacebookGraphException exception) {
            System.out.println(exception.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Testing the SearchPost class ---\n");
        String appID = "<Enter your application ID here>";
        String appSecret = "<Enter your application secret here>";
        try {
            SearchPost postSearcher = new SearchPost(appID, appSecret);
            String postID = "<Enter your post ID here>";
            System.out.println("Total likes on the post: " + postSearcher.getLikesCount(postID));
            System.out.println("Total comments on the post: " + postSearcher.getCommentsCount(postID));
            System.out.println("Total shares on the post: " + postSearcher.getSharesCount(postID));
        } catch (FacebookOAuthException exception) {
            System.out.println(exception.getMessage());
        }
    }
}