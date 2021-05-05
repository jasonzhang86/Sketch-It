# Sketch It
Sketch It is a drawing application that allows the user to draw lines and shapes on a canvas. The functionality is very similar to Windows Draw. <br>

The user may save a drawing as a .txt file and export it to the local computer. The user may also open such a .txt file in Sketch It to view, modify or continue the drawing.

Developer Journal:

0. Added Resize Option: Resize a shape by first clicking the bottom-right corner and then drag it to resize.
(Works best with Circle and Rectangle; Line's endpoint is hard to click on, try the thickest line)
Note: MUST CLICK THE BOTTOM-RIGHT CORNER FIRST! Otherwise app treates drag as moving the shape

1. When the application starts no tool is selected, pressing select will prompt a window and activate select mode.
Pressing Ok or Cancel on the new Window will close it but application remains in select mode. To quit select mode press ESC on keyboard.

2. Since there is no icon for "no tool is selected", if user click select then quit with esc, the select button will have blue shades around it as if it's selected. Pressing another button solves this.
    (Maybe use toggleButton ?)

3. When selecting a shape, the selected shape is moved to the top of the canvas (if the user clicked a shape that is under another shape, it will be moved to the top). This indicates what the selected shape is.

4.  For the style bar (thickness & line style), it seems only the last button pressed has the blue shade around it indicating it's selected. So the user may have to remeber what thickness / style they selected. (for instance, if I select normal thickness then dash line, then only the dash line button has shade but current thickness is set to normal as well)

5. When the user press new/load/quit under menu, a window prompts out asking if the user wants to save current drawing. The options are:
    1. Ok - Save and execute
    2. No - Do not save and execute
    3. Cancel - Cancel execution
    
6. When the application starts, it assumes we have an empty drawing. Thus if user quits immediately after the application starts, there will be a window prompting out asking if user wants to save (even though nothing has been drawn)


openjdk version "11.0.8" 2020-07-14 <br>
macOS 10.14.6 (MacBook Pro 2019)
