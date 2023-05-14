//      EDIT PROMPT       //
var dialog = document.querySelector('#editDialog');
var showDialogButtons = document.querySelectorAll('.show-dialog-edit');
if (!dialog.showModal) {
    dialogPolyfill.registerDialog(dialog);
}
showDialogButtons.forEach(function (button) {
    button.addEventListener('click', function () {
        dialog.showModal();
    });
});
dialog.querySelector('.close-edit').addEventListener('click', function () {
    dialog.close();
});

function editPrompt(prompt) {
    console.log("--Edit Prompt--");
    console.log(prompt);
    document.getElementById('editPromptId').value = prompt.id;
    document.getElementById('editPromptTitle').value = prompt.title;
    document.getElementById('editPromptSummary').value = prompt.summary;
    document.getElementById('editPromptContent').value = prompt.content;

    let tagsStr = '';
    prompt.tags.forEach(element => {
        tagsStr += element.tag + ',';
    });
    document.getElementById('editPromptTags').value = tagsStr;
}


//      DELETE PROMPT     //
var deleteDialog = document.querySelector('#deleteDialog');
var showDialogButtons = document.querySelectorAll('.show-dialog-delete');
if (!deleteDialog.showModal) {
    dialogPolyfill.registerDialog(deleteDialog);
}
showDialogButtons.forEach(function (button) {
    button.addEventListener('click', function () {
        deleteDialog.showModal();
    });
});
deleteDialog.querySelector('.close-delete').addEventListener('click', function () {
    deleteDialog.close();
});

function deletePrompt(prompt) {
    console.log("--Delete Prompt--");
    console.log(prompt);
    document.getElementById('deletePromptId').value = prompt.id;
    document.getElementById('deletePromptTitle').textContent = prompt.title;
    document.getElementById('deletePromptSummary').textContent = prompt.summary;
    document.getElementById('deletePromptContent').textContent = prompt.content;

    var tagsDiv = document.getElementById('deletePromptTags');
    tagsDiv.innerHTML = ''; //clear existing elements

    //const tagsArray = prompt.tags;
    prompt.tags.forEach(element => {
        var mdlChipTextSpan = document.createElement("span");
        mdlChipTextSpan.classList.add("mdl-chip__text");
        mdlChipTextSpan.textContent = element.tag;

        var mdlChipSpan = document.createElement("span");
        mdlChipSpan.classList.add("mdl-chip");
        mdlChipSpan.appendChild(mdlChipTextSpan);

        tagsDiv.appendChild(mdlChipSpan);
    })
}