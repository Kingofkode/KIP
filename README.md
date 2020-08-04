# KIP - Keep in touch

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
1. [Schema](#Schema)

## Overview
### Description
Have you ever received a text out of the blue from a friend you haven't talked to in a while? KIP makes those experiences easy. KIP makes it easy to maintain meaningful relationships with friends and family. KIP suggests ways to reconnect with others and encourage in person interaction through smart and realtime suggestions. 

Are you and a friend in the same area during lunch hour? KIP will automatically suggest you ask that person to grab lunch.

Haven't talked to that friend from chemistry class in a while? KIP will suggest you ask her about her recent trip to Colorado.

### App Evaluation
- **Category:** Social Networking
- **Mobile:** This app would be primarily developed for mobile as it relies on location for realtime smart suggestions.
- **Story:** User can add friends on KIP from their contacts or by signing into social media. KIP then analyzes friends' activity such as location, social media, music taste, and previous conversations to suggest ways to reconnect.
- **Market:** Any individual could choose to use this app.
- **Habit:** This app could be used as often or unoften as the user wanted depending on how deep their social life is, and what exactly they're looking for.
- **Scope:** First we would start with connecting people based on social media posts and news. Eventually the app could use a backend that processes previous conversations on KIP to suggest new ways to start a conversation. i.e. If KIP learns that you and a friend share an interest in cars, KIP will automatically suggest a conversation starter when a news story about cars is published. Such as: "Hey! Did you see the release of the Tesla cyber truck?"

## Product Spec
### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* User logs in to access chats and suggestions
* Link Facebook/Instagram account
* User can add a new friend by searching for their username
* User can send texts
* Profile pages for each user with the last time you connected, social medias, profile picture, username
* Account page (username, profile picture, signing in and out)

**Optional Nice-to-have Stories**

* Connect spotify for music conversation starters
* Group chats
* Add friends from social media or contacts
* Show shared interests in profile section
* Connect a news API
* Users can send photos
* Chats and Friend requests are pushed from Parse Server
### 2. Screen Archetypes

* Login 
* Register - User signs up or logs into their account
   * Upon Download/Reopening of the application, the user is prompted to log in to gain access to their profile information.
   * Asks for username, password, and birthday.
* Messaging Screen - Chat for users to communicate (direct 1-on-1)
* Profile Screen 
   * Allows user to upload a photo and link their social media accounts.
* Add Friend Screen
   * Allows user to search for friends by username and send a request
   * Respond to incoming requests
* Settings Screen
   * Lets people change profile picture
   * Name
   * Log in / out

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Chats
* Suggestions

Top Left:
* Profile/Settings

Top Right:
* Button to start new chat

**Flow Navigation** (Screen to Screen)
* Forced Log-in -> Account creation if no log in is available
* Jumps to Chat
* If a suggestion is tapped, jump to chat view
* Profile -> Text field to be modified. 
* Settings -> Toggle settings

## Wireframes
<img src="https://raw.githubusercontent.com/Kingofkode/KIP/master/KIP%20Wireframe%201.jpg" width=1000><br> <img src="https://raw.githubusercontent.com/Kingofkode/KIP/master/KIP%20Wireframe%202.jpg" width=1000><br>

## Schema 
### Models
#### Message

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the message (default field) |
   | createdAt     | DateTime | date when message is sent (default field) |
   | conversationID | String | pointer to conversation that this message belongs to
   | senderId      | User Pointer| message author |
   | body          | String   | message body |
   | image         | File     | image from sender (optional) |
   
   #### Coversation

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the conversation (default field) |
   | createdAt     | DateTime | date when first message is sent (default field) |
   | updatedAt     | DateTime | date when last message is sent (default field) |
   | lastMessage   | Message Pointer | last message sent |
   | members  | [User Pointer] | list of users in the conversation |
   
   #### User

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the user (default field) |
   | createdAt     | DateTime | date when user is created (default field) |
   | friendIds | [User Pointer] | unique ids for the users the user is friends with
   | profileImage  | File     | profile image for user (optional) |
   | instagramAccount  | String     | instagram profile for user (optional) |
   | conversationIds | [Conversation Pointer] | unqiue ids for the conversations the user is a part of
   
   #### FriendRequest

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the request (default field) |
   | createdAt     | DateTime | request when request is sent (default field) |
   | senderId      | User Pointer| request author |
   | recipientId | User Pointer | request destination
   
### Networking
#### List of network requests by screen
   - Conversations Fragment
      - (Read/GET) Query all conversations that belong to the user
         ```java
        ParseQuery<Conversation> conversationQuery = ParseQuery.getQuery(Conversation.class);
        conversationQuery.include(Conversation.KEY_MEMBER_IDS);
        conversationQuery.include(Conversation.KEY_LAST_MESSAGE);

        conversationQuery.findInBackground(new FindCallback<Conversation>() {
          @Override
          public void done(List<Conversation> conversations, ParseException e) {
            if (e != null) {
              Log.e(TAG, "Issue with getting conversations", e);
              return;
            }

            for (Conversation conversation : conversations) {
              try {
                ParseUser sender = conversation.getLastMessage().getSender().fetchIfNeeded();
                Message message = conversation.getLastMessage();
                // Todo: Populate RecyclerView with data
              } catch (ParseException ex) {
                ex.printStackTrace();
              }
            }
          }
        });
         ```
   - New Message Fragment
      - (Read/GET) User's friends
      - (Create/POST) Create a new conversation and message object
   - Add friends activity
      - (Read/GET) Query user's by user name
      - (Create/POST) Send friend request
   - Message activity
      - (Read/GET) all messages betweens users
      - (Create/POST) new messages
   - Profile activity
      - (Update/PUT) Unfriend a profile
      - (Update/PUT) Update user profile image
# App Expectations
- [x] Your app has multiple views
- [x] Your app interacts with a database (e.g. Parse)
- [x] You can log in/log out of your app as a user
- [x] You can sign up with a new user profile
- [x] Somewhere in your app you can use the camera to take a picture and do something with the picture (e.g. take a photo and share it to a feed, or take a photo and set a user’s profile picture)
- [x] Your app integrates with a SDK (Facebook SDK)
- [x] Your app contains at least one more complex algorithm (talk over this with your manager)
- [x] Your app uses gesture recognizers (e.g. double tap to like, e.g. pinch to scale)
- [x] Your app use an animation (doesn’t have to be fancy) (e.g. fade in/out, e.g. animating a view growing and shrinking)
- [x] Your app incorporates an external library to add visual polish
