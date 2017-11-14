# nrg-dn1
First nrg homework


### 1. Introduction
In recent years, a 3D scanning technology is becoming available to everyday people and in already getting integrated in
everyday devices (such as: Kinect and Project Tango). This is also one of the reasons why more and more 3D scanning
data in form of point clouds is publically available (either 3D scans of individual objects or 3D scans of landscape -
LiDAR).

The main goal of this homework is to get familiar with point cloud processing steps used for creating a 3D mesh
representation from a digitized data. The basic processing steps are:
1. aligning multiple scans (not part of homework),
2. estimation of surface normals (optional),
3. removal of outlaying points (optional),
4. 3D surface reconstruction.

As part of this homework you are expected to implement steps 2. – 4. which are explained in more details in the
following sections. Some parts of the homework are optional. The homework must be concluded in 2 weeks. You will
have to defend the homework at the laboratory exercises. The homework is worth 10 % of the final grade. The defense of
the homework after the deadline lowers the maximum worth: 1-week extension: 7 %, 2-week extension 5 %.

### 2. Point cloud data
You may use publically accessable point-cloud data from the Internet. There are several datasets availible online [2]. You
may also use LiDAR terrain point cloud dataset of Slovenia [2].

### 3. Estimation of surface normals (optional)
An optional part of the homework is to implement the estimation of surface normals in the pointcloud. You may use one
of the methods presented at the lectures (general approach or [3]). You can use hierarhical approach for faster kNN
selection.

### 4. Removal of outlaying points (optional)
To improve the final surface reconstruction it is good to remove most of outlaying points which mostly represent noise.
You can use the approach presented at the lectures – Eigen value approach – where you observe the variance along
individual principal axis (𝜆", 𝜆$).

### 5. 3D surface reconstruction
The main goal of this homework is to implement a direct polygonial mesh reconstruction from the point cloud.
Implement 2D Delaunay triangulation [4] algorithm. In Delauney tirangulation only use x and y components of points,
but use all three components for final model creation.
Optional:
(1) Implement 3D Delanuay triangulation [5].
(2) Implement Angle optimal triangulation.

### 6. Output and Implementation
The output of the implementation has to be a 3D mesh model stored in OBJ [6] data format. The implmentation can be
done in any programming language.

#### References
[1] List of online point cloud dataset can be found at: http://www.pointclouds.org/news/2013/01/07/point-cloud-data-sets/
[2] LiDAR terrain point cloud dataset for Slovenia is availible at: http://gis.arso.gov.si/evode/profile.aspx?id=atlas_voda_Lidar@Arso 
[3] Hugues Hoppe, Tony DeRose and Tom Duchampy. Surface Reconstruction from Unorganized Points. ACM SIGGRAPH 1992 Proceedings, 71-78.
[4] Delaunay triangulation: https://en.wikipedia.org/wiki/Delaunay_triangulation
[5] Pavel Maur. Delaunay Triangulation in 3D, Technical Report No. DCSE/TR-2002-02, 2002.
[6] Definition of AliasWawefron OBJ file format: https://en.wikipedia.org/wiki/Wavefront_.obj_file
