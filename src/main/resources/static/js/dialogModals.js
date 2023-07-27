//      UTILITY       //

let devMode = true;
function devTestLog(msg, object) {
    if (devMode == true) {
        console.log(msg);
        console.log(object);
    }
}

//These two are used in the HTML
function createMDLChips(prompt, tagsDiv) {
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
function setUpDialogButtons(dialogId, openModalClass, closeModalClass) {
    var dialog = document.querySelector('#'+ dialogId);
    var showDialogButtons = document.querySelectorAll('.' + openModalClass);

    if (!dialog.showModal) {
        dialogPolyfill.registerDialog(dialog);
    }
    showDialogButtons.forEach(function (button) {
        button.addEventListener('click', function () {
            dialog.close();
            dialog.showModal();
        });
    });
    dialog.querySelector('.' + closeModalClass).addEventListener('click', function () {
        dialog.close();
    });
}

//      READ MORE       //
function readMorePrompt(prompt) {
    //devTestLog("--Read More Prompt--",prompt); //dev

    document.getElementById('readMorePromptTitle').textContent = prompt.title;
    document.getElementById('readMorePromptSummary').textContent = prompt.summary;
    document.getElementById('readMorePromptContent').textContent = prompt.content;

    var tagsDiv = document.getElementById('readMorePromptTags');
    tagsDiv.innerHTML = ''; //clear existing elements

    createMDLChips(prompt,tagsDiv);
}

//      EDIT PROMPT       //
function editPrompt(prompt) {
    //devTestLog("--Edit Prompt--",prompt); //dev

    setInputValueAndTriggerEvent('editPromptId', prompt.id);
    setInputValueAndTriggerEvent('editPromptTitle', prompt.title);
    setInputValueAndTriggerEvent('editPromptSummary', prompt.summary);
    setInputValueAndTriggerEvent('editPromptContent', prompt.content);

    let tagsStr = '';
    prompt.tags.forEach(element => {
        tagsStr += element.tag + ',';
    });
    //let tagsStr = prompt.tags.map(element => element.tag).join(',');
    setInputValueAndTriggerEvent('editPromptTags', tagsStr);

}

function setInputValueAndTriggerEvent(inputId, value) {
    let input = document.getElementById(inputId);
    input.value = value;
    input.dispatchEvent(new Event('input'));
}

//      DELETE PROMPT     //

function deletePrompt(prompt) {
    //devTestLog("--Delete Prompt--",prompt); //dev

    document.getElementById('deletePromptId').value = prompt.id;
    document.getElementById('deletePromptTitle').textContent = prompt.title;
    document.getElementById('deletePromptSummary').textContent = prompt.summary;
    document.getElementById('deletePromptContent').textContent = prompt.content;

    var tagsDiv = document.getElementById('deletePromptTags');
    tagsDiv.innerHTML = ''; //clear existing elements

    createMDLChips(prompt,tagsDiv);
}