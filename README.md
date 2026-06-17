# Sokoban Project

This is a project for the course Programming Project at UPM. It's an implementation of the Sokoban game in Java using Agile/XP methodologies. The project is structured using the MVC design pattern and includes two controllers:

- **TUIController**: This allows the user to play the game using the terminal. *Only a demo version (first sprint) is available.*
- **GUIController**: This allows the user to play the game using a graphical interface.


## Authors

- Ivan Collado Tome (ivan.collado@alumnos.upm.es)
- Daniel Garcia Rodriguez (d.grodriguez@alumnos.upm.es)
- Victor Garrote Martin (victor.garrote@alumnos.upm.es)

## Build and run

To build and run the project you start by clean compiling the project with Maven:

```bash
mvn clean compile
```

Then you can run the project, it will start the GUI controller by default:

```bash
mvn exec:java
```


You can specify the controller you want to use with the following command:

```bash
mvn exec:java -Dexec.args="gui"
mvn exec:java -Dexec.args="tui"
```

### Packaging

You can also build a jar file with the following command:

```bash
mvn package
```

And run it with, it will start the GUI controller by default:

```bash
java -jar target/sokoban-1.0-SNAPSHOT.jar
```

You can also specify the controller you want to use with the following command:

```bash
java -jar target/sokoban-1.0-SNAPSHOT.jar gui
java -jar target/sokoban-1.0-SNAPSHOT.jar tui
```


## Application controls

### Game controls

- **WASD**: Move the player up, left, down, and right.
- **R**: Reset the level.
- **U**: Undo the last move.
- **ESC**: Pause the game, open pause menu

*Reset and undo are also available in the pause menu.*

### Menu controls

- **W**: Move up in the menu.
- **S**: Move down in the menu.
- **ENTER/SPACE**: Select the current option.

## File management

### Game levels

The level files are embedded in the project's Classpath (`src/main/resources/levels/`). They are copied to the classpath during the build process. To update the levels or add new ones you will need to modify the files in the `src/main/resources/levels/` directory and rebuild the project.

### Game saves

The game saves use the `JFileChooser` component. This allows the user to select the path they want to place the save file. And the same applies for loading a save, the user can select the file they want to load. This allows the user to have unlimited saves and we considered file sharing between users.

## File formats

All the files used in the game are text files `*.txt` with the following formats:

### Level file format

- **Line 1:** Level name (string)
- **Line 2:** Level row, column (two integers separated by a comma ej. `10,10`)
- **Following lines:** Map representation using the following characters:
  - `+`: Wall
  - ` `: Empty space
  - `W`: WarehouseMan
  - `#`: Box
  - `*`: Goal

### Save file format

- **Line 1:** Level number (integer)
- **Line 2:** Game points (integer)
- **Line 3:** Moves made in the level, using the following characters:
  - `w`: Move WarehouseMan up
  - `a`: Move WarehouseMan left
  - `s`: Move WarehouseMan down
  - `d`: Move WarehouseMan right


*We save the moves made in the level so we can simulate the game state when loading a save file till the position of the WarehouseMan, the boxes and the level steps are the same as when the game was saved.*
