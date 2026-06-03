document.addEventListener("DOMContentLoaded", () =>{
	// =======================================
	// モーダル開閉処理
	// =======================================
	const openButton = document.getElementById("openAddTaskModal");
	const closeButton = document.getElementById("closeAddTaskModal");
	const cancelButton = document.getElementById("cancelAddTaskModal");
	const modal = document.getElementById("todoModal");
	const overlay = document.getElementById("modalOverlay");
	
	const openModal = () => {
		modal.classList.add("is-open");
		overlay.classList.add("is-open");
	};
	
	const closeModal = () => {
		modal.classList.remove("is-open");
		overlay.classList.remove("is-open");
	};
	
	openButton.addEventListener("click", openModal);
	closeButton.addEventListener("click", closeModal);
	cancelButton.addEventListener("click", closeModal);
	overlay.addEventListener("click", closeModal);
	

	// =======================================
	// 優先度フィルター切り替え処理
	// =======================================
	const priorityButtons = document.querySelectorAll(".priority-filter-item");

	priorityButtons.forEach((button) => {
		button.addEventListener("click", () => {
			
			// すべてのボタンからactiveを外す
			priorityButtons.forEach((btn) => {
				btn.classList.remove("is-active");
			});
			
			// クリックしたボタンだけactiveにする
			button.classList.add("is-active");
			
			// 押された優先度を取得する
			const selectedPriority = button.dataset.priority;
			
			// 最新のタスクカード一覧を取得する
			const taskCards = document.querySelectorAll(".task-card");
			
			// タスクカードを絞り込む
			taskCards.forEach((card) => {
				const taskPriority = card.dataset.priority;
				
				if (selectedPriority === "ALL" || taskPriority === selectedPriority) {
					card.style.display = "";
				} else {
					card.style.display = "none";
				}
			});
		});
	});
	
	// =======================================
	// チェックボックスによる完了表示切り替え
	// =======================================
	const taskCheckboxes = document.querySelectorAll(".task-checkbox");
	
	taskCheckboxes.forEach((checkbox) => {
		checkbox.addEventListener("change", async () => {
			const taskCard = checkbox.closest(".task-card");
			const todoId = taskCard.dataset.todoId;
			const checked = checkbox.checked;
			
			// CSRFトークンを取得する
			const csrfToken = document.querySelector('meta[name="_csrf"]').content;
			const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;
			
			try {
				const response = await fetch(`/todos/${todoId}/state`, {
					method: "POST",
					headers: {
						"Content-Type": "application/x-www-form-urlencoded",
					[csrfHeader]: csrfToken
					},
					body: `state=${checked}`
				});
				
				if (!response.ok) {
					throw new Error("状態更新に失敗しました。");
				}
				
				// 状態変更に成功したら、現在の一覧からタスクカードを消す
				taskCard.remove();
			} catch (error) {
				console.error(error);
				
				// DB更新に失敗したら表示を元に戻す
				checkbox.checked = !checked;
			}
		})
	});
	
	// =======================================
	// タスク編集処理
	// =======================================
	const editModal = document.getElementById("editModal");
	const editModalOverlay = document.getElementById("editModalOverlay");
	const cancelEditButton = document.getElementById("cancelEditButton");
	const closeEditButton = document.getElementById("closeEditButton");
	const editTaskForm = document.getElementById("editTaskForm");
	
	const editTodoId = document.getElementById("editTodoId");
	const editTitle = document.getElementById("editTitle");
	const editMemo = document.getElementById("editMemo");
	const editDueDate = document.getElementById("editDueDate");
	
	let editTargetCard = null;
	let editTargetId = null;
	
	const openEditModal = (taskCard) => {
		editTargetCard = taskCard;
		editTargetId = taskCard.dataset.todoId;
		
		// 編集対象の値をモーダルにセットする
		editTodoId.value = editTargetId;
		editTitle.value = taskCard.dataset.title || "";
		editMemo.value =  taskCard.dataset.memo || "";
		editDueDate.value = taskCard.dataset.dueDate || "";
		
		// 優先度をセットする
		const priority = taskCard.dataset.priority;
		const priorityRadio = document.querySelector(`input[name="edit-Priority"][value="${priority}"]`);
		
		if (priorityRadio) {
			priorityRadio.checked = true;
		}
		
		editModal.classList.add("is-open");
		editModalOverlay.classList.add("is-open");
	}
	
	const closeEditModal = () => {
		editTargetCard = null;
		editTargetId = null;
		
		editModal.classList.remove("is-open");
		editModalOverlay.classList.remove("is-open");
	};
	
	document.querySelectorAll(".task-edit-icon-button").forEach((button) => {
		button.addEventListener("click", () => {
			const taskCard = button.closest(".task-card");
			openEditModal(taskCard);
		});
	});

	cancelEditButton.addEventListener("click", closeEditModal);
	editModalOverlay.addEventListener("click", closeEditModal);
	
	if (closeEditButton) {
		closeEditButton.addEventListener("click", closeEditModal);
	}

	editTaskForm.addEventListener("submit", async (event) => {
		event.preventDefault();
		
		if (!editTargetId || !editTargetCard) {
			return;
		}
		
		const selectedPriority = document.querySelector('input[name="edit-priority"]:checked');
		
		if (!selectedPriority) {
			alert("優先度を選択して下さい。");
			return;
		}
		
		const csrfToken = document.querySelector('meta[name="_csrf"]').content;
		const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;
		
		const formData = new URLSearchParams();
		formData.append("title", editTitle.value);
		formData.append("memo", editMemo.value);
		formData.append("dueDate", editDueDate.value);
		formData.append("priority", selectedPriority.value);

		try {
			const response = await fetch(`/todos/${editTargetId}/edit`, {
		    	method: "POST",
		        headers: {
					"Content-Type": "application/x-www-form-urlencoded",
		        	[csrfHeader]: csrfToken
		    	},
				body: formData
		    });
			
		    if (!response.ok) {
		    	throw new Error("編集に失敗しました");
		    }
			
			// 画面上の表示を更新する
			editTargetCard.querySelector(".task-title").textContent = editTitle.value;
			editTargetCard.querySelector(".task-memo").textContent = editMemo.value;
			
			const dateElement = editTargetCard.querySelector(".task-date");
			if (dateElement) {
				dateElement.textContent = editDueDate.value;
			}
			
			const priorityElement = editTargetCard.querySelector(".task-priority");
			priorityElement.textContent = selectedPriority.value;
			
			priorityElement.classList.remove(
				"task-priority-low",
				"task-priority-medium",
				"task-priority-high"
			)
			
			if (selectedPriority.value === "LOW") {
				priorityElement.classList.add("task-priority-low");
			} else if (selectedPriority.value === "MEDIUM") {
				priorityElement.classList.add("task-priority-medium");
			} else if (selectedPriority.value === "HIGH") {
				priorityElement.classList.add("task-priority-high");
			}
			
			// data属性も更新する
			editTargetCard.dataset.title = editTitle.value;
			editTargetCard.dataset.memo = editMemo.value;
			editTargetCard.dataset.dueDate = editDueDate.value;
			editTargetCard.dataset.priority = selectedPriority.value;
			
			closeEditModal();
		
		} catch (error) {
			console.error(error);
		    alert("タスクの編集に失敗しました。");
		}
	});
	
	// =======================================
	// タスク削除処理
	// =======================================
	const deleteModal = document.getElementById("deleteModal");
	const deleteModalOverlay = document.getElementById("deleteModalOverlay");
	const cancelDeleteButton = document.getElementById("cancelDeleteButton");
	const confirmDeleteButton = document.getElementById("confirmDeleteButton");
	
	let deleteTargetCard = null;
	let deleteTargetId = null;
	
	const openDeleteModal = (taskCard) => {
		deleteTargetCard = taskCard;
		deleteTargetId = taskCard.dataset.todoId;
		
		deleteModal.classList.add("is-open");
		deleteModalOverlay.classList.add("is-open");
	};
	
	const closeDeleteModal = () => {
		deleteTargetCard = null;
		deleteTargetId = null;
		
		deleteModal.classList.remove("is-open");
		deleteModalOverlay.classList.remove("is-open");
	};
	
	document.querySelectorAll(".task-delete-icon-button").forEach((button) => {
		button.addEventListener("click", () => {
			const taskCard = button.closest(".task-card");
			openDeleteModal(taskCard);
		});
	});
	
	cancelDeleteButton.addEventListener("click", closeDeleteModal);
	deleteModalOverlay.addEventListener("click", closeDeleteModal);
	
	confirmDeleteButton.addEventListener("click", async () => {
		if (!deleteTargetId || !deleteTargetCard) {
			return;
		}
		
		const csrfToken = document.querySelector('meta[name="_csrf"]').content;
		const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

		try {
		        const response = await fetch(`/todos/${deleteTargetId}/delete`, {
		            method: "POST",
		            headers: {
		        	[csrfHeader]: csrfToken
		    	}
		    });

		    if (!response.ok) {
		    	throw new Error("削除に失敗しました");
		    }

		    deleteTargetCard.remove();
			
			const remainingTasks = document.querySelectorAll(".task-card");
			const emptyMessage = document.getElementById("emptyMessage");
			
			if (remainingTasks.length === 0) {
				emptyMessage.classList.remove("is-hidden");
			}
			
			closeDeleteModal();
		
		} catch (error) {
			console.error(error);
		    alert("タスクの削除に失敗しました。");
		}
	});
});