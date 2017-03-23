package Auth;

/*
 * Author: Honey Kakkar
 * Created on: February 14, 2017
 * Package: Auth
 * Project: Facebook Explorer
 */

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.FacebookClient.AccessToken;
import com.restfb.Version;

// This class represents a Facebook client.
// It contains methods to obtain the authorization required to make the API calls through access tokens.

public class AuthorizedClient {

    // accessToken represents an access token and expiration date pair.
    private AccessToken accessToken;

    // fbClient specifies how a Facebook Graph API client must operate.
    private FacebookClient fbClient;

    // Default constructor to get a default Facebook client
    public AuthorizedClient() {
        this.fbClient = new DefaultFacebookClient(Version.LATEST);
    }

    // Constructor to get a Facebook client with a given access token
    public AuthorizedClient(String accessToken) {
        this.fbClient = new DefaultFacebookClient(accessToken, Version.LATEST);
    }

    // Method to get the current access token
    public AccessToken getAccessToken() {
        return this.accessToken;
    }

    // Method to get the instance of current Facebook client
    public FacebookClient getFbClient() {
        return this.fbClient;
    }

    // Method to obtain the authorization using application token
    public void appAuthorization(String appID, String appSecret) {
        // Obtaining application access token
        this.accessToken = this.fbClient.obtainAppAccessToken(appID, appSecret);

        // Re-initializing Facebook client with the application access token
        this.fbClient = new DefaultFacebookClient(this.accessToken.getAccessToken(), appSecret, Version.LATEST);
    }

    // Method to obtain the authorization using application token
    public void userAuthorization(String appID, String appSecret, String redirectURI, String verificationCode) {
        if (this.accessToken == null) {
            AccessToken tempToken = fbClient.obtainUserAccessToken(appID, appSecret, redirectURI, verificationCode);
            FacebookClient tempClient = new DefaultFacebookClient(tempToken.getAccessToken(), Version.LATEST);

            this.accessToken = tempClient.obtainExtendedAccessToken(appID, appSecret, tempToken.getAccessToken());
            this.fbClient = new DefaultFacebookClient(this.accessToken.getAccessToken(), Version.LATEST);
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Testing the AuthorizedClient class ---\n");
        System.out.println("--- Testing the authorization by application access token ---\n");
        String appID = "<Enter your application ID here>";
        String appSecret = "<Enter your application secret here>";
        AuthorizedClient authorize = new AuthorizedClient();

        authorize.appAuthorization(appID, appSecret);
        System.out.println("Access token type: " + authorize.getAccessToken().getTokenType());
        System.out.println("Access token value: " + authorize.getAccessToken().getAccessToken());
    }
}