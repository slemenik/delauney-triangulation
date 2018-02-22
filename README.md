# Delauney triangulation of 3D point cloud points
A homework project for course of Advanced computer graphics, Faculty of Computer and Information Science, University of Ljubljana, Slovenia 


### 1. Introduction
In recent years, a 3D scanning technology is becoming available to everyday people and in already getting integrated in
everyday devices (such as: Kinect and Project Tango). This is also one of the reasons why more and more 3D scanning
data in form of point clouds is publically available (either 3D scans of individual objects or 3D scans of landscape -
LiDAR).

The main goal of this homework is to get familiar with point cloud processing steps used for creating a 3D mesh
representation from a digitized data. The basic processing steps are:
1. aligning multiple scans (not part of homework),
2. 3D surface reconstruction.

### 2. Point cloud data
You may use publically accessable point-cloud data from the Internet. There are several datasets availible online [1]. You
may also use LiDAR terrain point cloud dataset of Slovenia [2].

### 3. 3D surface reconstruction
The main goal of this homework is to implement a direct polygonial mesh reconstruction from the point cloud.
Implement 2D Delaunay triangulation [3] algorithm. In Delauney tirangulation only use x and y components of points,
but use all three components for final model creation.

### 4. Output and Implementation
The output of the implementation has to be a 3D mesh model stored in OBJ [4] data format. The implmentation can be
done in any programming language.

#### References
[1] List of online point cloud dataset can be found at: http://www.pointclouds.org/news/2013/01/07/point-cloud-data-sets/
[2] LiDAR terrain point cloud dataset for Slovenia is availible at: http://gis.arso.gov.si/evode/profile.aspx?id=atlas_voda_Lidar@Arso 
[3] Delaunay triangulation: https://en.wikipedia.org/wiki/Delaunay_triangulation
[4] Definition of AliasWawefron OBJ file format: https://en.wikipedia.org/wiki/Wavefront_.obj_file
