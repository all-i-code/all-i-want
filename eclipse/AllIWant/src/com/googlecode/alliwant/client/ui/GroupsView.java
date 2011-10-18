/**
 * @file GroupsView.java
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
package com.googlecode.alliwant.client.ui;

import com.google.gwt.user.client.ui.IsWidget;
import com.googlecode.alliwant.client.i18n.AiwConstants;
import com.googlecode.alliwant.client.i18n.AiwMessages;

public interface GroupsView extends JhbView {
  void setHeader(IsWidget header);
  void setNumMyGroups(int num);
  void setMyGroupName(int index, String name);
  void setMyGroupDescription(int index, String description);
  void setMyGroupMemberCount(int index, String memberCount);
  void setMyGroupInviteCount(int index, String inviteCount);
  void setNumGroups(int num);
  void setGroupName(int index, String name);
  void setGroupDescription(int index, String description);
  void setGroupOwner(int index, String owner);
  void setNumInvites(int num);
  void setInviteName(int index, String name);
  void setInviteOwner(int index, String owner);
  void setInviteEmail(int index, String email);
  AiwConstants getAiwc();
  AiwMessages getAiwm();
  void setPresenter(Presenter presenter);
  interface Presenter {
    void inviteMember(int index);
    void deleteGroup(int index);
    void leaveGroup(int index);
    void acceptInvite(int index);
    void declineInvite(int index);
  }
}
