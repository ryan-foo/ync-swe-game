# Bomber Kong

We will use [LibGDX](https://libgdx.badlogicgames.com/) and Android Studio in order to develop the game.

We will write our code and work in Android Studio. 

### Starting

```shell
git clone git@github.com:ryan-foo/ync-swe-game.git
```

After cloning to your local, use Android Studio to open the project. The last minute of [this video](https://www.youtube.com/watch?v=a8MPxzkwBwo) will show you how to setup Desktop Build (very important for fast workflow later on!)

### Folders of interest 

`bomberkong/core/src/com/bomberkong/game`
We will spend most of our time in this folder as we will implement majority of the game logic here. We can take input using LibGDX and then let the library handle the interface between devices and the game program.

The net effect is that we can test on computer (which is much faster to debug behaviour etc) and then easily port over to Android controls, rather than waiting for the Virtual Android to boot up etc. I am paranoid, but I am not an android user.

`bomberkong/android`
When we have implemented enough features, if there are Android specific requirements we can place code for that here. However, majority should be on core.

`assets`
Self explanatory, contains audio and image assets that will be used by the game.

### Project

You can click on the [Projects](https://github.com/ryan-foo/ync-swe-game/projects) icon to see how the project is organized.

We add tasks. These tasks are for us to complete, in order to complete our project.

Tasks can belong to one of five categories: admin, gameplay, UI/assets, input/output, networking.

Admin involves tasks such as setting up the main game project (that everybody should clone and work on), maintaining the board. 
Gameplay -- implementing the game's data model and the objects, as well as their behaviour. 
UI / Assets -- finding assets and also implementing the view for the game. 
Input / Output -- managing input/output, interface between UI/Assets and Gameplay. 
Networking -- related to multiplayer aspect of game (interface with matchmaking functionality).

I expect that we will also spend a non-insignificant amount of time on ensuring our stuff can interface well together. So let's leave enough time to accomplish our tasks and then glue them. When we can, assume that the other tasks have finished and build with dummy versions, so that we can keep moving.

Creating a task is done on the project screen by making a new card, then converting that card into an "Issue". We then set the label on the task by clicking on the name of the card.

Anyone can take off the tasks (assign yourself as the person doing the task, and move it to in-progress), and we can pre-fill the tasks. We can prune the board accordingly as well if tasks overlap.

Before you begin researching / working on a task, please comment on the task and let others know you are working on the task to prevent duplicate work. (I.E, assign yourself to the task.)

Try to make tasks as exclusionary as possible from other tasks to prevent duplicate work, and think through which tasks will depend on others to be complete. For example, one cannot add assets to the game if the assets have not yet been found and put in Google Drive. (which is a task in itself.)

### LibGDX project setup

![LibGDX Project Setup](/images/LIBGDXProjectSetup.png)
