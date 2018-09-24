public  class Node<T> {

    private final T content;
    private final boolean isLeaf;

    public Node(
            final T content,
            final boolean isLeaf
    )
    {
        this.content = content;
        this.isLeaf = isLeaf;
    }

    public T getContent() {
        return content;
    }

    public boolean isLeaf() {
        return isLeaf;
    }
}
