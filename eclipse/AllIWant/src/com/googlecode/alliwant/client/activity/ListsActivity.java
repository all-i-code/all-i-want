/**
 * @file ListsActivity.java
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
package com.googlecode.alliwant.client.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.googlecode.alliwant.client.ClientFactory;
import com.googlecode.alliwant.client.StringUtils;
import com.googlecode.alliwant.client.event.InfoEvent;
import com.googlecode.alliwant.client.event.ModelEvent;
import com.googlecode.alliwant.client.event.ModelListEvent;
import com.googlecode.alliwant.client.logging.Logging;
import com.googlecode.alliwant.client.model.ListItem;
import com.googlecode.alliwant.client.model.ListOwner;
import com.googlecode.alliwant.client.model.ListPermission;
import com.googlecode.alliwant.client.model.User;
import com.googlecode.alliwant.client.model.WishList;
import com.googlecode.alliwant.client.place.ListsPlace;
import com.googlecode.alliwant.client.rpc.Manager;
import com.googlecode.alliwant.client.ui.ListsView;
import com.googlecode.alliwant.client.ui.widget.Confirm;
import com.googlecode.alliwant.client.ui.widget.smart.EditItemPopup;
import com.googlecode.alliwant.client.ui.widget.smart.EditListPopup;
import com.googlecode.alliwant.client.ui.widget.smart.ItemDetailPopup;

public class ListsActivity implements Activity, ListsView.Presenter {

  private ClientFactory cf;
  private ListsView view;
  private Manager manager;
  private Confirm confirm;
  private EditListPopup editListPopup;
  private EditItemPopup editItemPopup;
  private ItemDetailPopup itemDetailPopup;
  private User user;
  private int ownerId = -1;
  private WishList currentList = null;
  private Map<Integer, WishList> listMap = new HashMap<Integer, WishList>();
  private Map<Integer, ListOwner> ownerMap = new HashMap<Integer, ListOwner>();
  private List<Integer> permissionOwnerIds = new ArrayList<Integer>();
  
  public ListsActivity(ListsPlace place, ClientFactory cf) {
    this.cf = cf;
    view = cf.getListsView();
    manager = cf.getManager();
    confirm = cf.getConfirm(); 
    editListPopup = cf.getEditListPopup();
    editItemPopup = cf.getEditItemPopup(); 
    itemDetailPopup = cf.getItemDetailPopup();
  }
  
  // ==========================================================================
  // BEGIN: Activity methods
  // ==========================================================================
  
  @Override
  public String mayStop() {
    return null;
  }

  @Override
  public void onCancel() {
  }

  @Override
  public void onStop() {
  }

  @Override
  public void start(AcceptsOneWidget panel, EventBus eventBus) {
    view.setPresenter(this); 
    panel.setWidget(view.asWidget());
    addEventBusHandlers(eventBus);
    view.setHeader(cf.getHeader());
    view.showProcessingOverlay();
    cf.getManager().getCurrentUser();
  } // start //
  
  // ==========================================================================
  // END: Activity methods
  // ==========================================================================
  
  // ==========================================================================
  // BEGIN: ListsView.Presenter methods
  // ==========================================================================
  
  @Override
  public void userChanged() {
    view.showProcessingOverlay();
    ownerId = StringUtils.toInt(view.getOwner());
    view.setCanEditLists(permissionOwnerIds.contains(ownerId));
    manager.getLists(ownerId);
  }
    
  @Override
  public void addList() {
    editListPopup.show(new EditListPopup.Handler() {
      public void onSave(int listId, String name, String description) {
        addList(name, description);
      }
    });
  } // addList //
 
  @Override
  public void editList() {
    if (null == currentList) {
      cf.getAlert().show(view.getAiwc().noListSelected());
      return;
    }
    editListPopup.show(currentList, new EditListPopup.Handler() {
      public void onSave(int listId, String name, String description) {
        editList(listId, name, description);
      }
    });
  } // editList //

  @Override
  public void deleteList() {
    if (null == currentList) {
      cf.getAlert().show(view.getAiwc().noListSelected());
      return;
    }
    confirm.show(view.getAiwc().confirmDeleteList(), new Confirm.Handler() {
      public void onYes() {
        reallyDeleteList(currentList.getId());
      }
    });
  } // deleteList //
  
  @Override
  public void listChanged() {
    view.showProcessingOverlay();
    int lid = StringUtils.toInt(view.getList());
    currentList = listMap.get(lid);
    showList();
  }
    
  @Override
  public void addItem() {
    if (null == currentList) {
      cf.getAlert().show(view.getAiwc().noListSelected());
      return;
    }
    
    boolean isOwnList = (ownerId == user.getOwnerId());
    boolean canAdd = permissionOwnerIds.contains(ownerId);
    editItemPopup.show(isOwnList, canAdd, new EditItemPopup.Handler() {
      public void onSave(int itemId, String name, String category,
       String description, String url, boolean surprise) {
        addItem(name, category, description, url, surprise); 
      }
    }) ;
  } // addItem //
  
  @Override
  public void goToItemUrl(int index) {
    view.openURL(currentList.getItems().get(index).getUrl());
  }
   
  @Override
  public void itemDetail(int index) {
    ListItem item = currentList.getItems().get(index);
    if (permissionOwnerIds.contains(ownerId)) {
      editItemPopup.show(item, new EditItemPopup.Handler() {
        public void onSave(int itemId, String name, String category,
         String description, String url, boolean surprise) {
          editItem(itemId, name, category, description, url); 
        }
      });
    } else {
      itemDetailPopup.show(item, user, new ItemDetailPopup.Handler() {
        public void unPurchaseItem(int itemId) { unPurchase(itemId); }
        public void reserveItem(int itemId) { reserve(itemId); }
        public void purchaseItem(int itemId) { purchase(itemId); }
      });
    }
  } // itemDetail //
    
  @Override
  public void itemAction(int index) {
    ListItem item = currentList.getItems().get(index);
    if (item.getReservedByOwnerId() == user.getOwnerId())
      purchase(item.getId());
    else if (item.getPurchasedByOwnerId() == user.getOwnerId()) 
      unPurchase(item.getId());
    else 
      reserve(item.getId());
  } // itemAction //
  
  @Override
  public void editItem(int index) {
    ListItem item = currentList.getItems().get(index);
	  editItemPopup.show(item, new EditItemPopup.Handler() {
	    public void onSave(int itemId, String name, String category,
	     String description, String url, boolean surprise) {
	      editItem(itemId, name, category, description, url); 
	    }
	      });
  } // editItem //
  
  @Override
  public void deleteItem(int index) {
    final int itemId = currentList.getItems().get(index).getId();
    confirm.show(view.getAiwc().confirmRemoveItem(), new Confirm.Handler() {
      public void onYes() {
        reallyDeleteItem(itemId);
      }
    });
  } // deleteItem //
  
  // ==========================================================================
  // END: ListsView.Presenter methods
  // ==========================================================================

  private void addEventBusHandlers(EventBus eventBus) {
    {
      ModelEvent.Handler<User> handler = new ModelEvent.Handler<User>() {
        public void onModel(ModelEvent<User> event) {
          handleUser(event.getModel());
        }
      };
      ModelEvent.register(eventBus, User.class, handler);
    }
    
    {
      ModelListEvent.Handler<ListOwner> handler =
       new ModelListEvent.Handler<ListOwner>() {
        public void onModelList(ModelListEvent<ListOwner> event) {
          handleOwners(event.getModelList());
        }
      };
      ModelListEvent.register(eventBus, ListOwner.class, handler);
    }
    
    {
      ModelListEvent.Handler<WishList> handler =
       new ModelListEvent.Handler<WishList>() {
        public void onModelList(ModelListEvent<WishList> event) {
          handleLists(event.getModelList());
        }
      };
      ModelListEvent.register(eventBus, WishList.class, handler);
    }
    
    {
      ModelEvent.Handler<WishList> handler = new ModelEvent.Handler<WishList>() {
        public void onModel(ModelEvent<WishList> event) {
          handleList(event.getModel());
        }
      };
      ModelEvent.register(eventBus, WishList.class, handler);
    }
    
    {
      ModelListEvent.Handler<ListPermission> handler =
       new ModelListEvent.Handler<ListPermission>() {
        public void onModelList(ModelListEvent<ListPermission> event) {
          handlePermissions(event.getModelList());
        }
      };
      ModelListEvent.register(eventBus, ListPermission.class, handler);
    }
    
    {
      InfoEvent.Handler handler = new InfoEvent.Handler() {
        public void onInfo(InfoEvent event) {
          handleListDeleted();
        }
      };
      InfoEvent.register(eventBus, InfoEvent.LIST_DELETED, handler);
    }
  } // addEventBusHandlers //

  private void handleUser(User user) {
    if (user.getOwnerId() < 0) return;
    this.user = user;
    manager.getPermissions(user.getOwnerId(), true);
  }
 
  private void handlePermissions(List<ListPermission> permissions) {
    permissionOwnerIds.clear();
    permissionOwnerIds.add(user.getOwnerId());
    for (ListPermission p : permissions) permissionOwnerIds.add(p.getOwnerId());
    manager.getAvailableOwners(user.getOwnerId());
  }
  
  private void handleOwners(List<ListOwner> owners) {
    view.hideProcessingOverlay();
    view.clearOwners();
    ownerMap.clear();
    Logging.logger().info("handleOwners: " + owners.size());
    for (ListOwner owner : owners) {
      ownerMap.put(owner.getId(), owner);
      String nickname = owner.getNickname();
      if (owner.getId() == user.getOwnerId()) nickname = view.getAiwc().me();
      view.addOwnerItem(nickname, Integer.toString(owner.getId()));
    }
    userChanged();
  } // handleOwners //
  
  private void handleLists(List<WishList> lists) {
    view.clearLists();
    listMap.clear();
    for (WishList wl : lists) {
      if ((null != currentList) && (currentList.getId() == wl.getId())) 
        currentList = wl;
      listMap.put(wl.getId(), wl);
      view.addListItem(wl.getName(), Integer.toString(wl.getId()));
    }
    if (null != currentList) {
      view.setList(currentList.getId() + "");
      showList();
    }
    else {
      listChanged();
    }
  } // handleLists //
  
  private void handleList(WishList wl) {
    currentList = wl;
    manager.getLists(ownerId);
  }
  
  private void handleListDeleted() {
    currentList = null;
    manager.getLists(ownerId);
  }
 
  private void showList() {
    boolean ownList = user.getOwnerId() == ownerId;
    view.setOwnItemsVisible(ownList);
    view.setItemsVisible(!ownList);
    
    List<ListItem> items = currentList.getItems();
    if (ownList) {
      view.setNumOwnItems(items.size());
      for (int i = 0; i < items.size(); i++) {
        ListItem item = items.get(i);
        view.setOwnItem(i, item.getName());
        view.setOwnItemCategory(i, item.getCategory());
        view.setOwnItemLinkVisible(i, (item.getUrl().length() > 0));
      } // for all items //
    } else {
      view.setNumItems(items.size());
      for (int i = 0; i < items.size(); i++) {
        ListItem item = items.get(i);
        String extra = "";
        if (item.isSurprise()) extra = " (" + view.getAiwc().surprise() + ")";
        view.setItem(i, item.getName() + extra);
        view.setItemCategory(i, item.getCategory());
        view.setItemLinkVisible(i, (item.getUrl().length() > 0));
        
        // Set status
        String status = view.getAiwc().available();
        String by = view.getAiwc().me();
        boolean available = true;
        if (item.getReservedByOwnerId() > 0) {
          available = false;
          if (item.getReservedByOwnerId() != user.getOwnerId())
            by = item.getReservedBy();
          status = view.getAiwm().reservedBy(by);
        } else if (item.getPurchasedByOwnerId() > 0) {
          available = false;
          if (item.getPurchasedByOwnerId() != user.getOwnerId())
            by = item.getPurchasedBy();
          status = view.getAiwm().purchasedBy(by);
        }
        view.setItemStatus(i, status);
        
        // Set Action
        String action = "";
        if (available) {
          action = view.getAiwc().reserve();
        } else if (item.getReservedByOwnerId() == user.getOwnerId()) {
          action = view.getAiwc().purchase();
        } else if (item.getPurchasedByOwnerId() == user.getOwnerId()) {
          action = view.getAiwc().unPurchase();
        }
        view.setItemActionText(i, action);
        view.setItemActionVisible(i, (action.length() > 0));
      } // for all items //
    }
  } // showList //
  
  private void addList(String name, String description) {
    view.showProcessingOverlay();
    manager.addList(ownerId, name, description);
  } // addList //
  
  private void editList(int listId, String name, String description) {
    view.showProcessingOverlay();
    manager.updateList(listId, name, description);
  } // editList //
  
  private void addItem(String name, String category, String description,
   String url, boolean surprise) {
    view.showProcessingOverlay();
    manager.addItem(currentList.getId(), name, category, description, url,
     surprise);
  } // addItem //
  
  private void editItem(int itemId, String name, String category,
   String description, String url) {
    view.showProcessingOverlay();
    manager.updateItem(itemId, name, category, description, url);
  } // editItem //
  
  public void unPurchase(int id) {
    view.showProcessingOverlay();
    manager.unPurchaseItem(id);
  }
  
  public void purchase(int id) {
    view.showProcessingOverlay();
    manager.purchaseItem(id);
  }
  
   void reserve(int id) {
    view.showProcessingOverlay();
    manager.reserveItem(id);
  }
  
  private void reallyDeleteItem(int itemId) {
    view.showProcessingOverlay();
    manager.deleteItem(itemId);
  }
  
  private void reallyDeleteList(int listId) {
    view.showProcessingOverlay();
    manager.deleteList(listId);
  }
  
} // ListsActivity //
