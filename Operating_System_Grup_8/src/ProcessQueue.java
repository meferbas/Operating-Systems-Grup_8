

public class ProcessQueue implements IProcessQueue {
	private Node<ISpecialProcess> front;
	private Node<ISpecialProcess> back;

	public ProcessQueue() {
		front = back = null;
	}

	@Override
	public void enqueue(ISpecialProcess process) {
		Node<ISpecialProcess> tmp = new Node<ISpecialProcess>(process);
		if (isEmpty()) {
			front = back = tmp;
		} else {
			tmp.next = back;
			back.prev = tmp;
			back = tmp;
		}
	}

	@Override
	public ISpecialProcess dequeue() {
		if (!isEmpty()) {
			var deletedNode = front;
			front = front.prev;
			if (front != null)
				front.next = null;
			else
				back = null;
			return deletedNode.data;
		} else {
			return null;
		}
	}

	@Override
	public boolean isEmpty() {
		return front == null;
	}

	@Override
	public ISpecialProcess getFirstItem() {
		return this.front.data;
	}

	// Kuyruktaki tüm proseslerin bekleme zamanını 1 artırır. Bekleme süresi 20
	// saniyeyi aşan prosesleri döndürür.
	@Override
	public IProcessQueue increaseWaitingTime() {
		IProcessQueue timeOutProcesses = new ProcessQueue();
		Node<ISpecialProcess> processNode = front;

		while (processNode != null) {
			if (processNode.data.increaseWaitingTime()) {
				timeOutProcesses.enqueue(processNode.data);
			}
			processNode = processNode.prev;
		}

		return timeOutProcesses;
	}

	@Override
	public IProcessQueue search(int destinationTime) {
		IProcessQueue foundedProcesses = new ProcessQueue();
		if (this.isEmpty())
			return foundedProcesses;
		Node<ISpecialProcess> processNode = front;
		while (processNode.data.getDestinationTime() <= destinationTime) {
			if (processNode.data.getDestinationTime() == destinationTime)
				foundedProcesses.enqueue(processNode.data);
			processNode = processNode.prev;
			if (processNode == null)
				break;
		}
		return foundedProcesses;
	}

	@Override
	public void delete(ISpecialProcess process) {
		 if (process == null || front == null) {
		        return; // Process ya da kuyruk boşsa işlem yapma
		    }

		Node<ISpecialProcess> deletedNode = front;
		while (deletedNode.data != process) {
			deletedNode = deletedNode.prev;
			if (deletedNode == null)
				return;
		}

		if (deletedNode == front)
			front = front.prev;
		else if (deletedNode == back)
			back = back.prev;
		else {
			deletedNode.prev.next = deletedNode.next;
			deletedNode.next.prev = deletedNode.prev;
		}
	}
}
