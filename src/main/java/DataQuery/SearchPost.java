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
    private SearchPost(String appID, String appSecret) throws FacebookOAuthException {
        authorizedClient = new AuthorizedClient();
        authorizedClient.appAuthorization(appID, appSecret);
    }

    // Method to get the count of likes on a post with a given postID
    private Long getLikesCount(String postID) throws FacebookGraphException {
        // Obtaining the reference of authorized Facebook client required to make the API call
        FacebookClient fbClient = authorizedClient.getFbClient();

        // Fetches a single Graph API object, mapping the result to an instance of objectType (Post in this case).
        Post postWithLikes = fbClient.fetchObject(postID, Post.class, Parameter.with("fields", "likes.limit(0).summary(true)"));

        // Return the count of likes on the post with the given postID
        return postWithLikes.getLikesCount();
    }

    // Method to get the count of comments on a post with a given postID
    private Long getCommentsCount(String postID) throws FacebookGraphException {
        // Obtaining the reference of authorized Facebook client required to make the API call
        FacebookClient fbClient = authorizedClient.getFbClient();

        // Fetches a single Graph API object, mapping the result to an instance of objectType (Post in this case).
        Post postWithComments = fbClient.fetchObject(postID, Post.class, Parameter.with("fields", "comments.limit(0).summary(true)"));

        // Return the count of comments on the post with the given postID
        return postWithComments.getCommentsCount();
    }

    // Method to get the count of shares of a post with a given postID
    private Long getSharesCount(String postID) throws FacebookGraphException {
        // Obtaining the reference of authorized Facebook client required to make the API call
        FacebookClient fbClient = authorizedClient.getFbClient();

        // Fetches a single Graph API object, mapping the result to an instance of objectType (Post in this case).
        Post postWithShares = fbClient.fetchObject(postID, Post.class, Parameter.with("fields", "shares.limit(0).summary(true)"));

        // Return the count of comments on the post with the given postID
        return postWithShares.getSharesCount();
    }

    public static void main(String[] args) {
        System.out.println("--- Testing the SearchPost class ---\n");
        String appID = "<Enter your application ID here>";
        String appSecret = "<Enter your application secret here>";
        try {
            SearchPost postSearcher = new SearchPost(appID, appSecret);
            System.out.println("Total likes on the post: " + postSearcher.getLikesCount("<Enter your post ID here>"));
            System.out.println("Total comments on the post: " + postSearcher.getCommentsCount("<Enter your post ID here>"));
            System.out.println("Total shares on the post: " + postSearcher.getSharesCount("<Enter your post ID here>"));
        } catch (FacebookGraphException exception) {
            System.out.println(exception.getMessage());
        }
    }
}