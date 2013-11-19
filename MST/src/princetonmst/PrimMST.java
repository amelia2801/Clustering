/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package princetonmst;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import princetonstd.StdIn;
import princetonstd.StdOut;
import princetonstd.In;
import princetonstd.Queue;
import princetonstd.UF;
/**
 *
 * @author Abraham Krisnanda
 */
/******************************************************************************
 *  Compilation:  javac PrimMST.java
 *  Execution:    java PrimMST filename.txt
 *  Dependencies: EdgeWeightedGraph.java Edge.java Queue.java
 *                IndexMinPQ.java UF.java In.java StdOut.java
 *  Data files:   http://algs4.cs.princeton.edu/43mst/tinyEWG.txt
 *                http://algs4.cs.princeton.edu/43mst/mediumEWG.txt
 *                http://algs4.cs.princeton.edu/43mst/largeEWG.txt
 *
 *  Compute a minimum spanning forest using Prim's algorithm.
 *
 *  %  java PrimMST tinyEWG.txt 
 *  1-7 0.19000
 *  0-2 0.26000
 *  2-3 0.17000
 *  4-5 0.35000
 *  5-7 0.28000
 *  6-2 0.40000
 *  0-7 0.16000
 *  1.81000
 *
 *  % java PrimMST mediumEWG.txt
 *  1-72   0.06506
 *  2-86   0.05980
 *  3-67   0.09725
 *  4-55   0.06425
 *  5-102  0.03834
 *  6-129  0.05363
 *  7-157  0.00516
 *  ...
 *  10.46351
 *
 *  % java PrimMST largeEWG.txt
 *  ...
 *  647.66307
 *
 ******************************************************************************/

/**
 *  The <tt>PrimMST</tt> class represents a data type for computing a
 *  <em>minimum spanning tree</em> in an edge-weighted graph.
 *  The edge weights can be positive, zero, or negative and need not
 *  be distinct. If the graph is not connected, it computes a <em>minimum
 *  spanning forest</em>, which is the union of minimum spanning trees
 *  in each connected component. The <tt>weight()</tt> method returns the 
 *  weight of a minimum spanning tree and the <tt>edges()</tt> method
 *  returns its edges.
 *  <p>
 *  This implementation uses <em>Prim's algorithm</em> with an indexed
 *  binary heap.
 *  The constructor takes time proportional to <em>E</em> log <em>V</em>
 *  and extra space (not including the graph) proportional to <em>V</em>,
 *  where <em>V</em> is the number of vertices and <em>E</em> is the number of edges.
 *  Afterwards, the <tt>weight()</tt> method takes constant time
 *  and the <tt>edges()</tt> method takes time proportional to <em>V</em>.
 *  <p>
 *  For additional documentation, see <a href="/algs4/44sp">Section 4.4</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *  For alternate implementations, see {@link LazyPrimMST}, {@link KruskalMST},
 *  and {@link BoruvkaMST}.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class PrimMST {
    private Edge[] edgeTo;        // edgeTo[v] = shortest edge from tree vertex to non-tree vertex
    private double[] distTo;      // distTo[v] = weight of shortest such edge
    private boolean[] marked;     // marked[v] = true if v on tree, false otherwise
    private IndexMinPQ<Double> pq;
    ArrayList<Integer> mapLeft = new ArrayList<>(); // manipulating indexPQ.java
    ArrayList<Integer> mapRight = new ArrayList<>(); // manipulating indexPQ.java
    ArrayList<EdgeWeightedGraph> clusters = new ArrayList<>(); // penyimpanan pohon cluster yang terbentuk
    /**
     * Compute a minimum spanning tree (or forest) of an edge-weighted graph.
     * @param G the edge-weighted graph
     */
    public PrimMST(EdgeWeightedGraph G) {
        edgeTo = new Edge[G.V()];
        distTo = new double[G.V()];
        marked = new boolean[G.V()];
        pq = new IndexMinPQ<Double>(G.V());
        for (int v = 0; v < G.V(); v++) distTo[v] = Double.POSITIVE_INFINITY;

        for (int v = 0; v < G.V(); v++)      // run from each vertex to find
            if (!marked[v]) prim(G, v);      // minimum spanning forest

        // check optimality conditions
        assert check(G);
    }

    // run Prim's algorithm in graph G, starting from vertex s
    private void prim(EdgeWeightedGraph G, int s) {
        distTo[s] = 0.0;
        pq.insert(s, distTo[s]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            scan(G, v);
        }
    }

    // scan vertex v
    private void scan(EdgeWeightedGraph G, int v) {
        marked[v] = true;
        for (Edge e : G.adj(v)) {
            int w = e.other(v);
            if (marked[w]) continue;         // v-w is obsolete edge
            if (e.weight() < distTo[w]) {
                distTo[w] = e.weight();
                edgeTo[w] = e;
                if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
                else                pq.insert(w, distTo[w]);
            }
        }
    }

    /**
     * Returns the edges in a minimum spanning tree (or forest).
     * @return the edges in a minimum spanning tree (or forest) as
     *    an iterable of edges
     */
    public Iterable<Edge> edges() {
        Queue<Edge> mst = new Queue<Edge>();
        for (int v = 0; v < edgeTo.length; v++) {
            Edge e = edgeTo[v];
            if (e != null) {
                mst.enqueue(e);
            }
        }
        return mst;
    }

    /**
     * Returns the sum of the edge weights in a minimum spanning tree (or forest).
     * @return the sum of the edge weights in a minimum spanning tree (or forest)
     */
    public double weight() {
        double weight = 0.0;
        for (Edge e : edges())
            weight += e.weight();
        return weight;
    }


    // check optimality conditions (takes time proportional to E V lg* V)
    private boolean check(EdgeWeightedGraph G) {

        // check weight
        double totalWeight = 0.0;
        for (Edge e : edges()) {
            totalWeight += e.weight();
        }
        double EPSILON = 1E-12;
        if (Math.abs(totalWeight - weight()) > EPSILON) {
            System.err.printf("Weight of edges does not equal weight(): %f vs. %f\n", totalWeight, weight());
            return false;
        }

        // check that it is acyclic
        UF uf = new UF(G.V());
        for (Edge e : edges()) {
            int v = e.either(), w = e.other(v);
            if (uf.connected(v, w)) {
                System.err.println("Not a forest");
                return false;
            }
            uf.union(v, w);
        }

        // check that it is a spanning forest
        for (Edge e : edges()) {
            int v = e.either(), w = e.other(v);
            if (!uf.connected(v, w)) {
                System.err.println("Not a spanning forest");
                return false;
            }
        }

        // check that it is a minimal spanning forest (cut optimality conditions)
        for (Edge e : edges()) {

            // all edges in MST except e
            uf = new UF(G.V());
            for (Edge f : edges()) {
                int x = f.either(), y = f.other(x);
                if (f != e) uf.union(x, y);
            }

            // check that e is min weight edge in crossing cut
            for (Edge f : G.edges()) {
                int x = f.either(), y = f.other(x);
                if (!uf.connected(x, y)) {
                    if (f.weight() < e.weight()) {
                        System.err.println("Edge " + f + " violates cut optimality conditions");
                        return false;
                    }
                }
            }

        }

        return true;
    }
    
    public ArrayList<Edge> sortEdges (PrimMST tree) {
        ArrayList<Edge> arrayOfEdges = new ArrayList<>();
        for (Edge e : tree.edges()) {
            arrayOfEdges.add(e);
        }
        Collections.sort(arrayOfEdges, new CustomComparator());
        return arrayOfEdges;
    }
    
    public static class CustomComparator implements Comparator<Edge> {
            @Override
            public int compare(Edge t1, Edge t2) {
                int comp = 0;
               if(Math.abs(t1.weight())< Math.abs(t2.weight())){
                       comp = 1;
               }else if(Math.abs(t1.weight()) == Math.abs(t2.weight())){
                       comp = 0;
               }else if(Math.abs(t1.weight()) > Math.abs(t2.weight())){
                       comp = -1;
               }
               return comp;
            }
      }
    
    public EdgeWeightedGraph[] cutMSTtree (PrimMST mst, Edge cuttingEdge) {
        // E : edgeContainerLeft.size()
        EdgeWeightedGraph[] result = new EdgeWeightedGraph[2];
        ArrayList<Edge> edgeContainerLeft = new ArrayList<>();
        ArrayList<Edge> edgeContainerRight = new ArrayList<>();
        ArrayList<Edge> edgeForConstruct = new ArrayList<>();
        ArrayList<Edge> mstEdgeContainer = new ArrayList<>();
        ArrayList<Edge> tempEdgeContainer = new ArrayList<>();
        int cuttingEdgeLeft = cuttingEdge.either();
        int cuttingEdgeRight = cuttingEdge.other(cuttingEdgeLeft);
        int totalEdges=0;
        // Array List of Edge yang akan dibangun untuk pohon
        for (Edge e : mst.edges()) {
            totalEdges++;
            // memasukkan seluruh edge , kecuali edge yang dipotong
            if (!e.equals(cuttingEdge))
                mstEdgeContainer.add(e);
        }
        
        for (Edge e : mstEdgeContainer) {
            if (!e.equals(cuttingEdge)) {
                // mengambil pohon yang berhubungan dari sisi cuttingEdge.either()
                if (e.either() == cuttingEdgeLeft || e.other(e.either()) == cuttingEdgeLeft) {
                    edgeContainerLeft.add(e);
                }
                else if (e.either() == cuttingEdgeRight || e.other(e.either()) == cuttingEdgeRight) {
                    edgeContainerRight.add(e);
                }
                else {
                    // sisa pohon yang dalam
                    tempEdgeContainer.add(e);
                }
            }
        }
        
        // menghapus pohon yang sudah masuk edgeContainerLeft dan Right
        mstEdgeContainer = tempEdgeContainer;
        
//        System.out.println("total Edges" + totalEdges);
//        System.out.println("sisa Edges" + mstEdgeContainer.size());
        // iterate lebih dalam jika belum selesai
        ArrayList<Edge> tempIteratorContainer = new ArrayList<>();
        while (totalEdges-1 != (edgeContainerLeft.size() + edgeContainerRight.size())) {
            tempEdgeContainer = new ArrayList<>();
            for (Edge e : mstEdgeContainer) {
                for (Edge eLeft : edgeContainerLeft) {
                    if (e.either() == eLeft.either() || e.other(e.either()) == eLeft.either()
                            || e.other(e.either()) == eLeft.other(eLeft.either()) || e.other(e.either()) == eLeft.other(eLeft.either())) {
                        tempEdgeContainer.add(e);
                    }
                    else
                        tempIteratorContainer.add(e);
                }
            }
            // memindahkan seluruh elemen dari temp ke edgeContainerLeft
            for (Edge t : tempEdgeContainer) {
                edgeContainerLeft.add(t);
            }
            // hapus elemen di mst yang sudah pindah ke edgeContainerLeft
            mstEdgeContainer = tempIteratorContainer;
            if (mstEdgeContainer.size()==0) {
                // sudah selesai semua pohon sudah pindah
                break;
            }
            /***/
            tempEdgeContainer = new ArrayList<>();
            for (Edge e : mstEdgeContainer) {
                for (Edge eRight : edgeContainerRight) {
                    if (e.either() == eRight.either() || e.other(e.either()) == eRight.either()
                            || e.other(e.either()) == eRight.other(eRight.either()) || e.other(e.either()) == eRight.other(eRight.either())) {
                        tempEdgeContainer.add(e);
                    }
                }
            }
            // memindahkan seluruh elemen dari temp ke edgeContainerRight
            for (Edge t : tempEdgeContainer) {
                edgeContainerRight.add(t);
            }
            // hapus elemen di mst yang sudah pindah ke edgeContainerLeft
            mstEdgeContainer = tempIteratorContainer;
            if (mstEdgeContainer.size()==0) {
                // sudah selesai semua pohon sudah pindah
                break;
            }
        }
        // hitung jumlah verteks kiri dan edge kiri
        int nVLeft = countVertex(edgeContainerLeft);
        int nVRight = countVertex(edgeContainerRight);
        EdgeWeightedGraph cluster1 = new EdgeWeightedGraph(nVLeft);
        // encode edgeContainerLeft
        // isi cluster 1 dengan edgeContainerLeft
        for (Edge e : encodeEdges(edgeContainerLeft, mapLeft)) {
            cluster1.addEdge(e);
        }
        EdgeWeightedGraph cluster2 = new EdgeWeightedGraph(nVRight);
        // isi cluster 2 dengan edgeContainerRight
        for (Edge e : encodeEdges(edgeContainerRight, mapRight)) {
            cluster2.addEdge(e);
        }
        result[0] = cluster1;
        result[1] = cluster2;
        clusters.add(result[0]);
        clusters.add(result[1]);
        return result;
    }
    
    public PrimMST convertGraphtoTree(EdgeWeightedGraph eg){
        PrimMST p = new PrimMST(eg);
        return p;
    }
    
    public void MakeClusters(Edge cuttingEdge, ArrayList<EdgeWeightedGraph> clusters){
        for(EdgeWeightedGraph g : clusters){
            for(Edge e : g.edges()){
                if(e.equals(cuttingEdge)){                    
                    cutMSTtree(convertGraphtoTree(g), cuttingEdge);
                    clusters.remove(g);
                    break;
                }
            }
        }        
    }
    
    public int countVertex (ArrayList<Edge> edgeContainer) {
        ArrayList<Integer> vertexList = new ArrayList<>();
        for (Edge e : edgeContainer) {
            if (!vertexList.contains(e.either())) {
                vertexList.add(e.either());
            }
            if (!vertexList.contains(e.other(e.either()))) {
                vertexList.add(e.other(e.either()));
            }
        }
        return vertexList.size();      
    }
    
    public ArrayList<Edge> encodeEdges (ArrayList<Edge> source, ArrayList<Integer> map) {
        // merubah arrayList<Edge> source menjadi terurut dari 1..n,
        // sehingga dapat diurutkan menggunakan algoritma PrimMST Princeton
        ArrayList<Integer> vertexList = new ArrayList<>();
        ArrayList<Edge> encodedList = new ArrayList<>();
        // 1. kumpulkan semua vertex
        for (Edge e : source) {
            if (!vertexList.contains(e.either())) {
                vertexList.add(e.either());
            }
            if (!vertexList.contains(e.other(e.either()))) {
                vertexList.add(e.other(e.either()));
            }
        }
        // 2. sorting
        Collections.sort(vertexList, new CustomComparatorInt());
        // 3. MAP
        for (int i=0; i<vertexList.size(); i++) {
            // index (key) --> isi (value)
            // encode(value) --> map.indexOf(value)
            // decode(key) --> map.get(key)
            map.add(vertexList.get(i));
        }
        // 4. encode 
        // dari V ke Key
        for (Edge e : source) {
            int leftV = map.indexOf(e.either());
            int rightV = map.indexOf(e.other(e.either()));
            Edge tempEdge = new Edge(leftV, rightV, e.weight());
            encodedList.add(tempEdge);
            //System.out.println("temp" + tempEdge.toString());
        }
        return encodedList;
    }
    
    public String decodeEdges (EdgeWeightedGraph G, ArrayList<Integer> map) {
        System.out.println("decoded!");
        String NEWLINE = System.getProperty("line.separator");
        StringBuilder s = new StringBuilder();
        s.append(G.V() + " " + G.E() + NEWLINE);
        for (int v = 0; v < G.V(); v++) {
            s.append(map.get(v) + ": ");
            for (Edge e : G.adj(v)) {
                Edge decodedE = new Edge(map.get(e.either()), map.get(e.other(e.either())), e.weight());
                s.append(decodedE + "  ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }
    
    public static class CustomComparatorInt implements Comparator<Integer> {
        // posisi paling atas adalah yang bernilai paling kecil
        @Override
        public int compare(Integer t1, Integer t2) {
            int comp = 0;
           if((t1)<(t2)){
                comp = -1;
           }else if((t1) == (t2)){
                comp = 0;
           }else if((t1) > (t2)){
                comp = 1;
           }
           return comp;
        }
    }
    
    /**
     * Unit tests the <tt>PrimMST</tt> data type.
     */
    public static void main(String[] args) {
        EdgeWeightedGraph cluster1;
        EdgeWeightedGraph cluster2;
        EdgeWeightedGraph cluster3;
        EdgeWeightedGraph cluster4;
        In in = new In("/tinyEWG.txt");
        EdgeWeightedGraph G = new EdgeWeightedGraph(in);
        PrimMST mst = new PrimMST(G);
        System.out.println("Data :");
        for (Edge e : mst.edges()) {
            StdOut.println(e);
        }
        
        StdOut.printf("%.5f\n", mst.weight());
        System.out.println("urutkan: \n"+mst.sortEdges(mst).toString());
        Edge E0 = mst.sortEdges(mst).get(0);
        Edge E1 = mst.sortEdges(mst).get(6);
        Edge E2 = mst.sortEdges(mst).get(2);
        ArrayList<Edge> cuttingEdges = new ArrayList<>();
        cuttingEdges.add(E0);
        cuttingEdges.add(E1);
        cuttingEdges.add(E2);
        System.out.println(E0.toString());
        System.out.println(E1.toString());
        System.out.println(E2.toString());
        System.out.println("---");
        // create a new cluster
        System.out.println("hasil clustering");
        System.out.println("cluster kiri");
        cluster1 = mst.cutMSTtree(mst, E0)[0];
        System.out.println(mst.decodeEdges(cluster1, mst.mapLeft));
        PrimMST weight = new PrimMST(cluster1);
        System.out.println("rata-rata : " + weight.weight() / cluster1.E());
        
        System.out.println("\nhasil clustering");
        System.out.println("cluster kanan");
        cluster2 = mst.cutMSTtree(mst, E0)[1];
        System.out.println(mst.decodeEdges(cluster2, mst.mapRight));
        PrimMST weight2 = new PrimMST(cluster2);
        System.out.println("rata-rata : " + weight2.weight() / cluster2.E());
        
        System.out.println("\nhasil clustering -------------------------------");
        cluster3 = mst.cutMSTtree(mst.convertGraphtoTree(cluster1), E1)[0];
        System.out.println(mst.decodeEdges(cluster3, mst.mapRight));
        PrimMST weight3 = new PrimMST(cluster3);
        System.out.println("rata-rata : " + weight3.weight() / cluster3.E());
        
        System.out.println("\nhasil clustering -------------------------------");
        cluster4 = mst.cutMSTtree(mst.convertGraphtoTree(cluster1), E1)[1];
        System.out.println(mst.decodeEdges(cluster4, mst.mapRight));
        PrimMST weight4 = new PrimMST(cluster4);
        System.out.println("rata-rata : " + weight4.weight() / cluster3.E());
                
//        mst.cutMSTtree(mst, cuttingEdges.get(0));
//        System.out.println("print clusters");        
//        for(int i=0; i<mst.clusters.size(); i++){
//            System.out.println(mst.clusters.get(i).toString());            
//        }
//        System.out.println("------------------");
//        for(int i=1; i<cuttingEdges.size(); i++){
//            System.out.println("cuttingedge = "+cuttingEdges.get(i));
//            mst.MakeClusters(cuttingEdges.get(i), mst.clusters);
//        }
//        
//        for(int i=0; i<mst.clusters.size(); i++){
//            System.out.println(cuttingEdges.get(i));
//            System.out.println(mst.clusters.get(i).toString());            
//        }
    }
}
