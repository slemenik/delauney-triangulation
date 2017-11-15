import java.io.*;
import java.util.*;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        System.out.println("Started...");
        List<Point> pointList = getParsedData();
        List<Triangle> triangulation = getTriangulation(pointList);
        makeOBJ(triangulation, false);
        System.out.println("Finished.");
    }

    //1. Point cloud data
    private static List<Point> getParsedData() {
        //.asc only - format: x;y;z\n (e.g. ARSO Lidar DMR)
        List<Point> pointList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(new File("input.asc")))) {
            String line;
            int id = 1;
            while ((line = br.readLine()) != null) {
                String[] coordinatesString = line.split(";");
                Point p = new Point(Double.parseDouble(coordinatesString[0]),
                                    Double.parseDouble(coordinatesString[1]),
                                    Double.parseDouble(coordinatesString[2]),
                                    id++);
                pointList.add(p);
            }
        } catch (Exception e){
            System.out.println("izjema" + e);
        }
        return pointList;

    }

    //3D surface reconstruction.
    private static List<Triangle> getTriangulation(List<Point> pointList){

        //Bowyer–Watson algorithm
        int triangleID = 1;
        List<Triangle> triangulation = new ArrayList<>();
        //super-triangle (http://page.mi.fu-berlin.de/faniry/files/faniry_aims.pdf -> 4.1.)
        double M = getMaximumAbsoluteCoordinate(pointList);
        Triangle superTriangle = new Triangle(  new Point(3*M, 0,0, 10),//-1
                new Point(0,3*M, 0, 20),//-2
                new Point( -3*M, -3*M, 0, 30), 10);//-3
        triangulation.add(superTriangle);
        for (Point point : pointList) {
            System.out.println();
            System.out.println("tocka" + point);
            LinkedList<Triangle> badTriangles = new LinkedList<>();
            HashMap<Edge, Integer> badEdges = new HashMap<>(); //integer counts num of same edges
            for (Triangle triangle : triangulation) {
                if (inCircle(point, triangle.p1, triangle.p2, triangle.p3)) {
                    badTriangles.add(triangle);

                    if (badEdges.containsKey(triangle.e1)) {
                        badEdges.put(triangle.e1, badEdges.get(triangle.e1) + 1);
                    } else {
                        badEdges.put(triangle.e1, 1);
                    }

                    if (badEdges.containsKey(triangle.e2)) {
                        badEdges.put(triangle.e2, badEdges.get(triangle.e2) + 1);
                    } else {
                        badEdges.put(triangle.e2, 1);
                    }

                    if (badEdges.containsKey(triangle.e3)) {
                        badEdges.put(triangle.e3, badEdges.get(triangle.e3) + 1);
                    } else {
                        badEdges.put(triangle.e3, 1);
                    }
                }
            }

            //edge is not shared by any other triangles in badTriangles
            LinkedList<Edge> polygon = new LinkedList();
            for (Map.Entry<Edge, Integer> edge : badEdges.entrySet()) {
                if (edge.getValue() == 1) {
                    polygon.add(edge.getKey());
                }
            }
            System.out.println("poligon: " + polygon);
            System.out.println("pred remove bad: " + triangulation);
            triangulation.removeAll(badTriangles);
            System.out.println("po remove bad" + triangulation);

            for (Edge edge : polygon) {
                Triangle newTriangle = new Triangle(point, edge.p1, edge.p2, triangleID++);
                triangulation.add(newTriangle);
                System.out.println("nov" + newTriangle);
            }
        }
        System.out.println("Trikotnikov najprej " + triangulation.size() + triangulation);
        ArrayList<Triangle> solution = new ArrayList<>();
        for (Triangle triangle : triangulation){
            if (    !triangle.p1.equals(superTriangle.p1) &&
                    !triangle.p1 .equals( superTriangle.p2) &&
                    !triangle.p1 .equals( superTriangle.p3) &&
                    !triangle.p2 .equals( superTriangle.p1) &&
                    !triangle.p2 .equals( superTriangle.p2) &&
                    !triangle.p2 .equals( superTriangle.p3)&&
                    !triangle.p3 .equals( superTriangle.p1) &&
                    !triangle.p3 .equals( superTriangle.p2) &&
                    !triangle.p3 .equals( superTriangle.p3))
                solution.add(triangle);
        }
        System.out.println("Trikotnikov pol " + solution.size());
        return solution;
    }

    //Output and Implementation
    private static void makeOBJ(List<Triangle> triangles, boolean writeToFile) {
        System.out.println(writeToFile);
        HashMap<Integer, String> vertexMap = new HashMap<>();//id=>"v x y z"
        ArrayList<String> faceList = new ArrayList<>();
        for (Triangle triangle : triangles) {

            if (!vertexMap.containsKey(triangle.p1.id))
                vertexMap.put(triangle.p1.id, "v " + triangle.p1.x + " " + triangle.p1.y + " " + triangle.p1.z );
            if (!vertexMap.containsKey(triangle.p2.id))
                vertexMap.put(triangle.p2.id, "v " + triangle.p2.x + " " + triangle.p2.y + " " + triangle.p2.z );
            if (!vertexMap.containsKey(triangle.p3.id))
                vertexMap.put(triangle.p3.id, "v " + triangle.p3.x + " " + triangle.p3.y + " " + triangle.p3.z );

            String facesString = String.format("f %d %d %d", triangle.p1.id, triangle.p2.id, triangle.p3.id);
            if (!faceList.contains(facesString)) {
                faceList.add(facesString);
            }
        }
        PrintWriter writer = null;
        if (writeToFile) {
            try {
                writer = new PrintWriter("output.obj", "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (Map.Entry<Integer, String> entry : vertexMap.entrySet()) {
            if (writeToFile){
                assert writer != null;
                writer.println(entry.getValue());
            } else{
                System.out.println(entry.getValue());
            }
        }
        System.out.println();
        if (writeToFile) writer.println();

        for (String face : faceList){
            if (writeToFile){
                writer.println(face);
            } else {
                System.out.println(face);
            }
        }
        if (writeToFile) writer.close();
    }

    private static double getMaximumAbsoluteCoordinate(List<Point> pointList) {
        double M = 0.0; //absolute maximum
        for (Point point : pointList) {
            if (Math.abs(point.x) > M) M = Math.abs(point.x);
            if (Math.abs(point.y) > M) M = Math.abs(point.y);
        }
        return M;
    }

    private static boolean inCircle(Point pt, Point v1, Point v2, Point v3) {

        double ax = v1.x;
        double ay = v1.y;
        double bx = v2.x;
        double by = v2.y;
        double cx = v3.x;
        double cy = v3.y;
        double dx = pt.x;
        double dy = pt.y;

        double  ax_ = ax-dx;
        double  ay_ = ay-dy;
        double  bx_ = bx-dx;
        double  by_ = by-dy;
        double  cx_ = cx-dx;
        double  cy_ = cy-dy;
        double det=  (
                        (ax_*ax_ + ay_*ay_) * (bx_*cy_-cx_*by_) -
                        (bx_*bx_ + by_*by_) * (ax_*cy_-cx_*ay_) +
                        (cx_*cx_ + cy_*cy_) * (ax_*by_-bx_*ay_)
        );

        if (ccw ( ax,  ay,  bx,  by,  cx,  cy)) {
            return (det>0);
        } else {
            return (det<0);
        }

    }

    private static boolean ccw(double ax, double ay, double bx, double by, double cx, double cy) {//counter-clockwise
        return (bx - ax)*(cy - ay)-(cx - ax)*(by - ay) > 0;
    }

    public static class Triangle{

        Point p1,p2,p3;
        Edge e1, e2, e3;
        int id;

        Triangle(Point p1, Point p2, Point p3, int id)  {
            this.id = id;

            //point are ALWAYS from lowest to highest
            Point[] pointsArray = new Point[]{p1,p2,p3};
            Arrays.sort(pointsArray);
            this.p1 = pointsArray[0];
            this.p2 = pointsArray[1];
            this.p3 = pointsArray[2];

            this.e1 = new Edge(p1, p2, this);
            this.e2 = new Edge(p2, p3, this);
            this.e3 = new Edge(p3, p1, this);
        }

        @Override
        public boolean equals(Object obj) {
            Triangle tr = (Triangle) obj;
            return (tr.p1.equals(this.p1) && tr.p2.equals(this.p2) && tr.p3.equals(this.p3));
        }

        @Override
        public String toString() {

            return "T." + p1+","+p2+ ","+ p3;// + ": points(" + this.p1 + "; " + this.p2 + "; " + p3;
        }
    }

    static class Edge {

        Point p1,p2;
        Triangle t;

        Edge (Point p1, Point p2, Triangle t){
            this.t = t;
            //from lowest Point.id to highest
            if (p1.id < p2.id){
                this.p1 = p1;
                this.p2 = p2;
            } else if (p1.id > p2.id){
                this.p1 = p2;
                this.p2 = p1;
            }else {
                try {
                    throw new Exception();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public boolean equals(Object obj) {

            return ((Edge) obj).p1.equals(this.p1) && ((Edge) obj).p2.equals(this.p2);
        }

        @Override
        public int hashCode() {
            return (p1.toString() + " " + p2.toString()).hashCode();

        }

        @Override
        public String toString() {

            return "E."+p1+"," + p2 + "(" + t+")";
        }
    }

    public static class Point implements Comparable<Point>{

        double x, y, z;
        int id;

        Point(double x, double y, double z, int id) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.id = id;
        }

        @Override
        public boolean equals(Object obj) {
            Point p = (Point) obj;
            return (p.x == this.x && p.y == this.y && p.z == this.z);
        }

        @Override
        public String toString() {
            return "P" + id;// + "|" + "x= " + this.x + ", y = " + this.y + ", z = " + this.z;
        }

        @Override
        public int compareTo(Point o) {
            if (this.id > o.id ) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}


