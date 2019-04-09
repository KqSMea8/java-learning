package melon;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * @author Otis.z
 * @date 2019/3/28
 */
public class Solution {

    public boolean isSymmetric(TreeNode root) {
        if (root == null) {
            return true;
        }
        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        TreeNode O = new TreeNode(0);
        while (!queue.isEmpty()) {
            if (queue.size() != 1) {
                TreeNode l = queue.pollFirst();
                TreeNode r = queue.pollLast();
                if (l == O && r == O) {
                    continue;
                }
                if (l == null || r == null) {
                    if (l != r) {
                        return false;
                    }
                } else if (l.val != r.val) {
                    return false;
                } else {
                    queue.addFirst(l.right == null ? O : l.right);
                    queue.addFirst(l.left == null ? O : l.left);
                    queue.addLast(r.left == null ? O : r.left);
                    queue.addLast(r.right == null ? O : r.right);
                }
            } else {
                TreeNode node = queue.poll();
                queue.add(node.left);
                queue.add(node.right);
            }
        }

        return true;
    }


    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(2);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(3);
        root.right.left = new TreeNode(3);
        root.right.right = new TreeNode(4);
        root.left.right.left = new TreeNode(8);
        root.left.right.right = new TreeNode(9);
        root.right.right.left = new TreeNode(9);
        root.right.right.right = new TreeNode(8);

//        System.out.println(new Solution().isSymmetric(root));
    }
}
