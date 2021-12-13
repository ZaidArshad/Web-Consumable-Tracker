# Web-Consumable-Tracker
Project by Zaid Arshad. Developed from September to December 2021.

## Description
Web Consumable Tracker is a 2 part project that makes up a native web client and web server programmed in Java. The client makes use of Java Swing to display a GUI and the server uses Spring Boot to process calls to the REST API. The objective of this project is to keep track of food stocks and their expiry. The server saves the list of consumables between executions and the client calls to the server to display those items.

## Experience
This project taught me how to use Spring Boot, Swing and Gson. While the GUI may be bland it comes out to be neat and easy to navigate. I don’t usually showcase school projects on my personal GitHub but I thought that this taught me many different aspects of software development.

## Consumables
All consumables are stored on the server and will display on the client as shown.
<div align="center">
  <img src="https://user-images.githubusercontent.com/52565263/145733547-e4057a04-017e-4550-90d7-1636c60e7613.PNG" width="540"/>
</div>

## Scroll Bar
The scroll bar will show when there is not enough space on the screen to show an item.
<div align="center">
  <img src="https://user-images.githubusercontent.com/52565263/145736195-87e14bf4-8a1b-42b2-b76d-99f29da8ac55.gif" width="400"/>
</div>

## Separate Lists
The client has 4 different tabs to display food based on expiry.
<div align="center">
  <img src="https://user-images.githubusercontent.com/52565263/145736274-87decc96-aad7-40fa-be4d-f8bbd2f96c8c.gif" width="400"/>
</div>


## Adding an item
An item can be added with the add button at the bottom of the screen. A dialog will pop up asking for details of the consumable. The only field that can be left empty is notes.
<div align="center">
  <img src="https://user-images.githubusercontent.com/52565263/145736013-17f14739-8725-4f90-a24d-2b5f44f6115a.gif" width="400"/>
</div>

## Removing an item
An item can be removed by clicking the remove button.
<div align="center">
  <img src="https://user-images.githubusercontent.com/52565263/145736122-e9d0d542-1759-4ad2-90c6-0315a8336c84.gif" width="400"/>
</div>


## cURL Commands
The server can be accessed in any terminal that has cURL. An example of an item being added:

`curl -H "Content-Type: application/json" -X POST -d {\"type\":1,\"name\":\"Muffin\",\"notes\":\"\",\"price\":5.0,\"quantity\":3.5,\"expiryDate\":\"2021-12-24T00:00\"} localhost:8080/addItem/food`

while the server is running on a local machine. 

