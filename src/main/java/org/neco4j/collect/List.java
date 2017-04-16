package org.neco4j.collect;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class List<V> implements IndexedAddable<V, List<V>> {

    private final int _heapSize;
    private final Heap<V> _heap;
    private final List<V> _next;

    private final static List<?> EMPTY = new List<>(0, null, null);

    private List(int heapSize, Heap<V> heap, List<V> next) {
        _heapSize = heapSize;
        _heap = heap;
        _next = next;
    }

    @Override
    public boolean isEmpty() {
        return this == EMPTY;
    }

    @SuppressWarnings("unchecked")
    public static <V> List<V> empty() {
        return (List<V>) EMPTY;
    }

    public static <V> List<V> singleton(V v) {
        return List.<V>empty().prepend(v);
    }

    @SafeVarargs
    public static <V> List<V> of(V ... vs) {
        List result = List.<V>empty();
        for(int i = vs.length - 1; i >= 0; i--) {
            result = result.prepend(vs[i]);
        }
        return result;
    }

    @Override
    public List<V> prepend(V v) {
        if (isEmpty() || _heapSize != _next._heapSize) {
            return new List<>(1, new Leaf<>(v), this);
        } else {
            return new List<>(2 * _heapSize + 1,
                    new Branch<>(v, _heap, _next._heap),
                    _next._next);
        }
    }

    //TODO: use better algorithm, store Stack<Heap<V>> and rebuild whole nodes
    @Override
    public Opt<List<V>> putOpt(Integer index, V v) {
        return manipulateAt(index, current -> current.isEmpty()
                ? Opt.none()
                : Opt.some(current.removeFirstOpt().getOrFail().prepend(v)));
    }

    @Override
    public Opt<List<V>> addOpt(Integer index, V v) {
        return manipulateAt(index, current -> Opt.some(current.prepend(v)));
    }

    @Override
    public Opt<V> getOpt(Integer index) {
        if (index < 0 || isEmpty()) {
            return Opt.none();
        } else if (index < _heapSize) {
            return  Opt.some(_heap.get(_heapSize, index));
        } else {
            return _next.getOpt(index - _heapSize);
        }
    }

    @Override
    public Opt<List<V>> removeOpt(Integer index) {
        return manipulateAt(index, List::removeFirstOpt);
    }

    private Opt<List<V>> manipulateAt(int index, Function<List<V>, Opt<List<V>>> op) {
        if (index < 0) {
            return Opt.none();
        }
        Stack<V> stack = Stack.empty();
        List<V> current = this;
        while (! current.isEmpty() && index >= current._heapSize) {
           stack = stack.addAll(current._heap);
           index -= current._heapSize;
           current = current._next;
        }
        while(index-- > 0) {
            if (current.isEmpty()) {
                return Opt.none();
            }
            stack = stack.add(current.getOpt(0).getOrFail());
            current = current.removeFirstOpt().getOrFail();
        }

        Opt<List<V>> result = op.apply(current);
        if (result.isEmpty()) {
            return Opt.none();
        }
        current = result.getOrFail();

        while (! stack.isEmpty()) {
            current = current.prepend(stack.getOrFail());
            stack = stack.removeOpt().getOrFail();
        }
        return Opt.some(current);
    }

    public Opt<List<V>> removeFirstOpt() {
        if (isEmpty()) {
            return Opt.none();
        } else if (_heapSize == 1) {
            return Opt.some(_next);
        } else {
            Branch<V> branch = (Branch<V>) _heap;
            return Opt.some(new List<>(_heapSize/2, branch._left,
                    new List<>(_heapSize / 2, branch._right, _next)));
        }
    }

    @Override
    public long size() {
        return isEmpty() ? 0L : _heapSize + _next.size();
    }

    private static abstract class Heap<V> implements Iterable<V> {
        protected final V _value;

        private Heap(V value) {
            _value = value;
        }

        abstract V get(int size, int index);

        @Override
        public Iterator<V> iterator() {
            return new Iterator<V>() {
                private Stack<Heap<V>> _todo = Stack.singleton(Heap.this);

                @Override
                public boolean hasNext() {
                    return ! _todo.isEmpty();
                }

                @Override
                public V next() {
                    if (hasNext()) {
                        Heap<V> current = _todo.getOrFail();
                        _todo = _todo.removeOpt().getOrFail();
                        if (current instanceof Branch) {
                            Branch<V> branch = (Branch<V>) current;
                            _todo = _todo.add(branch._right).add(branch._left);
                        }
                        return current._value;
                    }
                    throw new NoSuchElementException();
                }
            };
        }
    }

    private static class Leaf<V> extends Heap<V> {
        private Leaf(V value) {
            super(value);
        }

        @Override
        V get(int size, int index) {
            if (size != 1 || index != 0) {
                throw new AssertionError("size " + size + " index " + index + " on Leaf");
            }
            return _value;
        }
    }

    private static class Branch<V> extends Heap<V> {
        private Heap<V> _left;
        private Heap<V> _right;

        private Branch(V value, Heap<V> left, Heap<V> right) {
            super(value);
            _left = left;
            _right = right;
        }

        @Override
        V get(int size, int index) {
            if (index == 0) {
                return _value;
            } else if (index <= size / 2) {
               return _left.get(size / 2, index - 1);
            } else {
               return _right.get(size / 2, index - size/2 - 1);
            }
        }
    }

}

/*

data RaList a = EMPTY | Node Int (Heap a) (RaList a)
data Heap a = Leaf a | Branch a (Heap a) (Heap a)

empty :: RaList a
empty = EMPTY

null :: RaList a -> Bool
null EMPTY = true
null _ = false

singleton :: a -> RaList a
singleton x = Node 0 (Leaf x) EMPTY

cons :: a -> RaList a -> RaList a
cons x (Node sz1 hp1 (Node sz2 hp2 tl))
    | sz1 == sz2 = Node (2 * sz1 + 1) (Branch x hp1 hp2) tl
cons x xs = Node 1 (Leaf x) xs

head :: RaList a -> a
head (Node _ (Leaf x) _) = x
head (Node _ (Branch x _ _) _) = x
head _ = error "EMPTY"

tail :: RaList a -> RaList a
tail (Node 1 _ tl) = tl
tail (Node sz (Branch _ left right) tl) =
   Node sub left (Node sub right tl) where sub = sz `div` 2
tail _ = error "EMPTY"

size :: RaList a -> Int
size (Node sz _ tl) = sz + size tl
size _ = 0

get :: RaList a -> Int -> a
get (Node sz hp tl) i | i < sz = getHeap sz hp i
                      | otherwise = get tl (i-sz)
get _ _ = error "EMPTY"

getHeap :: Int -> Heap a -> Int -> a
getHeap _ (Leaf x) 0 = x
getHeap _ (Branch x _ _) 0 = x
getHeap sz (Branch _ left right) k =
  if k <= sub then getHeap sub left (k-1)
              else getHeap sub right (k-sub-1) where
  sub = sz `div` 2

toList :: RaList a -> [a]
toList (Node _ hp tl) = addHeapToList hp $ toList tl
toList EMPTY = []

addHeapToList :: Heap a -> [a] -> [a]
addHeapToList (Leaf x) xs = x:xs
addHeapToList (Branch x left right) xs = x : addHeapToList left (addHeapToList right xs)

fromList :: [a] -> RaList a
fromList xs = Prelude.foldr cons EMPTY xs

map :: (a -> b) -> RaList a -> RaList b
map f (Node sz hp tl) = Node sz (mapHeap f hp) (map f tl) where
   mapHeap f (Leaf x) = Leaf (f x)
   mapHeap f (Branch x left right) = Branch (f x) (mapHeap f left) (mapHeap f right)
map _ _ = EMPTY

drop :: Int -> RaList a -> RaList a
drop n EMPTY = EMPTY
drop n (node @ Node sz hp tl)
    | n <= 0 = node
    | n >= sz = drop (n - sz) tl
drop n (Node sz (Branch _ left right) tl) = drop (n - 1) (Node sub left (Node sub right tl)) where
   sub = sz `div` 2

take :: Int -> RaList a -> RaList a
take _ EMPTY = EMPTY
take n _ | n <= 0 = EMPTY
take n (Node sz hp tl)
  | sz >= n = fromList $ Prelude.take n $ addHeapToList hp []
  | otherwise = addHeap hp $ take (n-sz) tl where
     addHeap (Leaf x) xs = cons x xs
     addHeap (Branch x left right) xs = cons x (addHeap left (addHeap right xs))

foldl :: (a -> b -> a) -> a -> RaList b -> a
foldl f x EMPTY = x
foldl f x xs = foldl f (f x $ head xs) (tail xs)

foldr :: (a -> b -> b) -> b -> RaList a -> b
foldr f x EMPTY = x
foldr f x (Node _ hp tl) = foldrHeap f (foldr f x tl) hp where
   foldrHeap f x (Leaf y) = f y x
   foldrHeap f x (Branch y left right) = f y $ foldrHeap f (foldrHeap f x right) left

filter :: (a -> Bool) -> RaList a -> RaList a
filter cond EMPTY = EMPTY
filter cond xs = let hd = head xs
                     tl = filter cond $ tail xs
                 in if cond hd then cons hd tl else tl

zipWith :: (a -> b -> c) -> RaList a -> RaList b -> RaList c
zipWith f EMPTY _ = EMPTY
zipWith f _ EMPTY = EMPTY
zipWith f xs ys = cons (f (head xs) (head ys)) $ zipWith f (tail xs) (tail ys)

zip :: RaList a -> RaList b -> RaList (a,b)
zip xs ys = zipWith (,) xs ys

unzip :: RaList (a,b) -> (RaList a, RaList b)
unzip xs = (map fst xs, map snd xs)

instance Show Show a => RaList a where
  show ra = show $ RaList.toList ra
*/