# Zamburak

This project is created by a group of software engineers on 4th semester at University of Southern Denmark. Zamburak is a 2D shooter / mage arena, where all enemies are controlled by an AI.

# Installing

To install the game, navigate to the file Bundle.properties in /SilentUpdate/src/main/resources/org/netbeans/modules/autoupdate/silentupdate/resources, the tag "org_netbeans_modules_autoupdate_silentupdate_update_center", must point to the path of the updates.xml file in /ModuleBuilder/target/netbeans_site. Furthermore before running the application, the ModuleBuilder must be deployed.

# Known bugs

Incoming spell collider only works on 1 point.
No bounce if an entity casts a spell before being hit.
The game does not work optimally with touchpad, so using a mouse is recommended.
If the player dies outside the map, he will be invincible for the rest of the game.
