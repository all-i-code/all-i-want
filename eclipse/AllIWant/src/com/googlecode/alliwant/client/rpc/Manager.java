/**
 * @file Manager.java
 * @author Adam Meadows
 *
 * Copyright 2011 Adam Meadows 
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
*/
package com.googlecode.alliwant.client.rpc;

public interface Manager {
  
  /**
   * Request the currently logged in user, or a url for them to login
   * @param currentURL the current URL (used to redirect back after login)
   */
  void getCurrentUser();
  
  /**
   * Request current AccessReqs
   */
  void getAccessRequests();

  /**
   * Request given ListOwner
   * @param ownerId ID of ListOwner
   */
  void getOwner(int ownerId);
 
  /**
   * Update the given ListOwner
   * @param ownerId ID of ListOwner
   * @param name new String name for ListOwner
   * @param nickname new String nickname for ListOwner
   */
  void updateOwner(int ownerId, String name, String nickname);
  
  /**
   * Approve the given AccessReq
   * @param reqId ID of the AccessReq to approve
   */
  void approveRequest(int reqId);
  
  /**
   * Deny the given AccessReq
   * @param reqId ID of the AccessReq to deny
   */
  void denyRequest(int reqId);


  /**
   * Add a new Group
   * @param name short Name for Group
   * @param description longer Description of Group
   */
  void addGroup(String name, String description);

  /**
   * Update a given Group
   * @param groupId ID of Group
   * @param name new Name
   * @param description new Description
   */
  void updateGroup(int groupId, String name, String description);

  /**
   * Get my Groups (or all Groups if admin)
   */
  void getGroups();
  
  /**
   * Invite someone to join a Group
   * @param groupId ID of Group
   * @param email Email address of potential member
   */
  void inviteMember(int groupId, String email);
  
  /**
   * Get my GroupInvitations (or all GroupInvitations if admin)
   */
  void getGroupInvites();

  /**
   * Accept the given GroupInvitation
   * @param inviteId ID of GroupInvitation
   */
  void acceptInvite(int inviteId);
 
  /**
   * Decline the given GroupInvitation
   * @param inviteId ID of GroupInvitation
   */
  void declineInvite(int inviteId);

  /**
   * Get list of all available owners
   * @param ownerId ID of the current Owner
   */
  void getAvailableOwners(int ownerId);
  
  /**
   * Get list of all WishLists for given owner
   * @param ownerId ID of the Owner
   */
  void getLists(int ownerId);

  void addList(int ownerId, String name, String description);
  void updateList(int listId, String name, String description);
  void deleteList(int listId);

  void addItem(int listId, String name, String category, String description,
   String url, boolean isSurprise);
  
  void updateItem(int listId, String name, String category, String description,
   String url);
  
  void deleteItem(int itemId);
  void reserveItem(int itemId);
  void purchaseItem(int itemId);
  void unPurchaseItem(int itemId);
  
} // Manager //
