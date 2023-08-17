//      UTILITY       //

let devMode = true;
function devTestLog(msg, object) {
    if (devMode == true) {
        console.log(msg);
        console.log(object);
    }
}

function setUpTimestamps() {
    let cardTimestamps = document.getElementsByClassName('lastModified-value');

    for (let i = 0; i < cardTimestamps.length; i++) {
        const element = cardTimestamps[i];
        element.textContent = getTimestamp(element.textContent);
    }

}

function getTimestamp(date) {
    const lastModified = new Date(date);
    const currentTime = new Date();

    const timeDifference = currentTime.getTime() - lastModified.getTime();
    const secondsDifference = Math.floor(timeDifference / 1000);

    if (secondsDifference < 60) {
        return secondsDifference === 1 ? "1 second ago" : secondsDifference + " seconds ago";
    } else if (secondsDifference < 3600) {
        const minutesDifference = Math.floor(secondsDifference / 60);
        return minutesDifference === 1 ? "1 minute ago" : minutesDifference + " minutes ago";
    } else if (secondsDifference < 86400) {
        const hoursDifference = Math.floor(secondsDifference / 3600);
        return hoursDifference === 1 ? "1 hour ago" : hoursDifference + " hours ago";
    } else if (secondsDifference < 604800) {
        const daysDifference = Math.floor(secondsDifference / 86400);
        return daysDifference === 1 ? "1 day ago" : daysDifference + " days ago";
    } else if (secondsDifference < 2592000) {
        const weeksDifference = Math.floor(secondsDifference / 604800);
        return weeksDifference === 1 ? "1 week ago" : weeksDifference + " weeks ago";
    } else {
        return "1 month ago";
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
    document.getElementById('readMoreCreatedAt').textContent = prompt.createdAt;
    document.getElementById('readMoreLastModified').textContent = getTimestamp(prompt.lastModified);

    var tagsDiv = document.getElementById('readMorePromptTags');
    tagsDiv.innerHTML = ''; //clear existing elements

    createMDLChips(prompt,tagsDiv);
}
//
function submitFormWithDate(form,elementId) {
    //enter user's current date/time into hidden input "submissionDate" field right before submitting
    const submissionDate = new Date(Date.now());
    setInputValueAndTriggerEvent(elementId, submissionDate.toISOString());
    document.forms[form].submit(); //need to manually submit
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
    document.getElementById('deletePromptCreatedAt').textContent = prompt.createdAt;
    document.getElementById('deletePromptLastModified').textContent = getTimestamp(prompt.lastModified);

    var tagsDiv = document.getElementById('deletePromptTags');
    tagsDiv.innerHTML = ''; //clear existing elements

    createMDLChips(prompt,tagsDiv);
}