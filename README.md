#Bitmap Manipulation

##Description
Sample Android application showing how to efficently load Bitmaps, create a parallax effect on an ImageView and implement a RecyclerView image grid.

##Implementation
- This application collects 20 photos from the user's device displaying 1 in a large parallax scrolling banner, and 19 in the RecyclerView grid layout.

###Bitmap Manipulation
- When first deciding how to maniputate or decode Bitmap we must first have an understanding of the following :
	- The source of the Bitmaps to be loaded (Resources folder, Internal Mem, Gallery)
	- The expected size of the Bitmaps being loaded. (20kb or 30mb)
	- The number of Bitmaps being loaded. (20, 30, 1)
	- What type of View the Bitmaps are going to be loaded into. (Recycler, List, Linear, ect.)
- Please note in this example the following apply :
	- Source : Gallery or ContentProvider Image MediaStore
	- Typical size varied greatly depending on the device and front or rear facing camera. Due to this I added an additional compression step to my decoding backgrounded task.
	- This example uses a set of 0 -> 20 Bitmap images.
	- The Bitmaps are to be loaded into a RecyclerView.
- Once these questions are answered and you have collected your images you must then start with the creation of a backgrounded task (typically AsyncTask) in order to decode either your File or your Resource.
- Within your backgrounded task you should execute the following line of code to extract and decode your File effectively turning it into a Bitmap.
		BitmapFactory.decodeFile(userPhotoFile.getAbsolutePath(), bitmapOptions);
- Once decoded if you have small file sizes you can simply load the Bitmap into your ImageView using the following : 
		ImageView.setImageResource(Bitmap);
- It is also highly recommended that you store such decoded Bitmaps in a Cache in order to prevent decoding and launching a Task each time you wish to render your image on screen. In this example I use the built in Android LRU Cache.
- As meantioned above, in this example the Bitmap is further reduced in size through the contraint of its width and height. Reducing the Bitmaps width and height will significently reduce the amount of memory the Bitmap is using. Therefore if you are using thumbnail sized images and your source is large, it is recommended you create a smaller copy of the Bitmap and load that instead of the source.

###Parallax Effect
- The parallax effect seen on the main banner image is acheived through the manipulation of the ImageView container's scroll functionality.
- In order for the main content view to seem faster when scrolling we simply need to listen to the parent's scroll events returned to us from a custom implementation of a ScrollView (ListenableScrollView).
- Finally we simply just set our container's vertical translation (current vertical position) to the (normal)current scroll position multiplied by our constant scroll factor in this case 0.7f. This will allow for our main content to scroll faster at 100% scroll speed and our ImageView to scroll at 70% scroll speed, making it look like our main content is scrolling past our ImageView.

###RecyclerView Implementation
- Very similar to a ListView, the RecyclerView requires an Adapter, ViewHolder for recycling, and a LayoutManager for the arrangement of elements within the view.

####RecyclerView.Adapter

- The RecyclerView.Adapter like the ListView will take in a list of elements and then ask how to display the elements within its view.
- Just like the ListView it is very important to understand the concept of what the ViewHolder pattern is :
	- This pattern very simply holds a reference to the last used view that has scrolled off-screen.
	- When the view is scrolling off-screen it effectively reuses the view instead of inflating a new view to be used.
	- This has shown to improve performance and reduce lag on ListViews and RecycleViews due to the fact that inflation is a heavy process and is always done on the UI thread.
	- Always make sure that the UI thread is doing as little work as possible so that the user is presented with the fastest smoothest experience possible.
- Within the <b>onCreateViewHolder</b> method we simply inflate a view to be reused throughout the scroll and lifecycle of the RecyclerView. It can be seen here that the ImageView is inflated via the LayoutInflator, and then returned.
- Within the <b>onBindViewController</b> method we simply populate or modify our View. In this case we are loading a Bitmap into our ImageView.
- Lastly just like with a ListView we return the number of elements to be displayed within or <b>getItemCount</b> method.



