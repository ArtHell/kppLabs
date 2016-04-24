package sample;

/**
 * sorting int java
 */
public class JavaSort {

  /**
   * interface for quicksort
   *
   * @param xs
   */
  void sort(int[] xs) {
    sort(xs, 0, xs.length - 1);
  }

  /**
   * quicksort algorithm
   *
   * @param xs
   * @param l
   * @param r
   */
  void sort(int[] xs, int l, int r) {
    int pivot = xs[(l + r) / 2];
    int a = l;
    int b = r;
    while (a <= b) {
      while (xs[a] < pivot) {
        a = a + 1;
      }
      while (xs[b] > pivot) {
        b = b - 1;
      }
      if (a <= b) {
        swap(xs, a, b);
        a = a + 1;
        b = b - 1;
      }
    }
    if (l < b) sort(xs, l, b);
    if (b < r) sort(xs, a, r);
  }

  /**
   * swaps ints in array
   *
   * @param arr
   * @param i
   * @param j
   */
  void swap(int[] arr, int i, int j) {
    int t = arr[i];
    arr[i] = arr[j];
    arr[j] = t;
  }
}
