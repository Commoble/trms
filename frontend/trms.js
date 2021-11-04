const statusDiv = document.getElementById('status-div');
const approvableRequestsDiv = document.getElementById('approvable-requests-div');

const yourRequestExpansionApprovalsTable = document.getElementById('your-request-expansion-approvals-table');
const yourRequestExpansionApprovalsTableBody = document.getElementById('your-request-expansion-approvals-table-body');
const approvableRequestExpansionApprovalsTable = document.getElementById('approvable-request-expansion-approvals-table');
const approvableRequestExpansionApprovalsTableBody = document.getElementById('approvable-request-expansion-approvals-table-body');

const eventTypeDropdown = document.getElementById('select-event-type');
const gradingFormatDropdown = document.getElementById('select-grading-format');
const eventCostInput = document.getElementById('event-cost-input');
const passingGradeInput = document.getElementById('passing-grade-input');
const eventDateInput = document.getElementById('event-date-input');
const eventAddressInput = document.getElementById('event-address-input');
const eventDescriptionInput = document.getElementById('event-description-input');
const workRelatedJustificationInput = document.getElementById('work-related-justification-input');
const workTimeMissedInput = document.getElementById('work-time-missed-input');
const submitNewRequestButton = document.getElementById('submit-new-request-button');
const newRequestFormDiv = document.getElementById('new-request-form-div');
const initRequestButton = document.getElementById('init-new-request-form-button');

const approvalSubmissionDiv = document.getElementById('approval-submission-div');
const approvalSubmissionComments = document.getElementById('approval-submission-comments');
const submitApprovalButton = document.getElementById('approval-submission-button')

const awardSubmissionDiv = document.getElementById('award-submission-div');
const awardSubmissionButton = document.getElementById('award-submission-button');

const newRequestInputs = document.getElementsByClassName('new-request-input');


    
document.getElementById('login-button').onclick = login;
loginEnterFunction = event => {
    if (event.keyCode === 13)
    {
        login();
    }
};
document.getElementById('username-input').addEventListener("keydown", loginEnterFunction);
document.getElementById('password-input').addEventListener("keydown", loginEnterFunction);

async function login()
{
    const username = document.getElementById('username-input').value;
    const password = document.getElementById('password-input').value;

    // request employee verification from web api
    // if verification succeeds, remember and request additional data
    // otherwise add some invalid-login html

    const loginResponse = await getLoginResponse(username, password);
    if (loginResponse.ok)
    {
        const userInfo = await loginResponse.json();
        onSuccesfulLogin(username, password, userInfo.name);
    }
    else
    {
        statusDiv.innerHTML = "<p id='login-fail-text'>Login failed; invalid username and/or password</p>";
    }
}

async function getLoginResponse(username, password)
{
    const headers = new Headers();
    headers.set('Authorization', 'Basic ' + btoa(username + ":" + password));
    let url = `http://localhost:7000/employees/login`;
    return fetch(url, {headers: headers});
}

function onSuccesfulLogin(username, password, fullName)
{
    const loginDiv = document.getElementById('login-div');
    loginDiv.innerHTML = "";

    const loggedInText = document.createElement('p');
    loggedInText.id = 'logged-in-text';
    loggedInText.innerText = `Welcome, ${fullName}. You are logged in as ${username}.`;
    loginDiv.appendChild(loggedInText);

    statusDiv.innerHTML = "";

    const statusText = document.createElement('p');
    statusText.id = 'view-or-manage-text';
    statusText.innerText = `View or manage reimbursement requests below.`;
    statusDiv.appendChild(statusText);

    const requestsDiv = document.getElementById('requests-div');
    requestsDiv.removeAttribute('hidden');

    const logoutLink = document.createElement('a');
    logoutLink.href = './trms.html';
    const logoutButton = document.createElement('button');
    logoutButton.innerText = 'Log Out';
    logoutLink.appendChild(logoutButton);
    loginDiv.appendChild(logoutLink);

    refreshRequests(username, password);

    const newRequestButton = document.getElementById('init-new-request-form-button');
    newRequestButton.onclick = ()=>initNewRequestForm(username, password);

    refreshApprovableRequests(username, password);
}

async function refreshRequests(username, password)
{
    document.getElementById('new-request-form-div').hidden = 'true';

    const headers = new Headers();
    headers.set('Authorization', 'Basic ' + btoa(username + ":" + password));
    const url = `http://localhost:7000/requests/`;

    const requestsResponse = await fetch(url, {headers: headers});
    const requestsResponseBody = await requestsResponse.json();

    // const yourRequestsTable = document.createElement('table');
    // const yourRequestsTableHeaderRow = document.createElement('tr');
    // yourRequestsTable.appendChild(yourRequestsTableHeaderRow);
    
    const yourRequestTableBody = document.getElementById('your-requests-table-body');
    yourRequestTableBody.innerHTML = "";

    let currentButtonHolder = {
        currentButton: null
    };

    for (let request of requestsResponseBody)
    {
        let tr = document.createElement('tr');

        let submissionDate = timeStringToLocalDateString(request.submissionTime);
        let eventDate = timeStringToLocalDateString(request.eventTime);

        let eventDescription = request.eventDescription;
        let shortEventDescription = eventDescription.length > 40 ? eventDescription.substring(0, 40) : eventDescription;

        let submissionDateCell = document.createElement('td');
        submissionDateCell.innerText = submissionDate;

        let eventDescriptionCell = document.createElement('td');
        eventDescriptionCell.innerText = shortEventDescription;

        let eventDateCell = document.createElement('td');
        eventDateCell.innerText = eventDate;

        let statusCell = document.createElement('td');
        let status = request.status.replace(/_/g, ' ');
        statusCell.innerText = status;

        let actionsCell = document.createElement('td');
        let viewMoreButton = document.createElement('button');
        viewMoreButton.innerText = "View Details";

        let toggleFunction = ()=>
        {
            if (viewMoreButton.innerText == "View Details")
            {
                viewMoreButton.innerText = "Close";
                if (currentButtonHolder.currentButton != null)
                {
                    currentButtonHolder.currentButton.onclick();
                }
                currentButtonHolder.currentButton = viewMoreButton;
                // populate request expansion
                let requestExpansionDiv = document.getElementById('your-request-expansion-div');
                requestExpansionDiv.removeAttribute('hidden');

                document.getElementById('your-request-expansion-submission-date').innerText = submissionDate;
                document.getElementById('your-request-expansion-event-description').innerText = (eventDescription);
                document.getElementById('your-request-expansion-event-date').innerText = (eventDate);
                document.getElementById('your-request-expansion-status').innerText = (status);
                document.getElementById('your-request-expansion-event-type').innerText = (request.eventTypeName);
                document.getElementById('your-request-expansion-event-cost').innerText = (request.eventCost);
                document.getElementById('your-request-expansion-reimbursement-amount').innerText = (request.reimbursementAmount);
                document.getElementById('your-request-expansion-grading-format').innerText = (request.gradingFormatName)
                document.getElementById('your-request-expansion-passing-grade').innerText = (request.passingGrade);
                document.getElementById('your-request-expansion-work-related-justification').innerText = (request.workRelatedJustification);
                document.getElementById('your-request-expansion-work-time-missed').innerText = (request.workTimeMissed);

                // populate approvals
                yourRequestExpansionApprovalsTableBody.innerHTML = "";
                if (request.approvals && request.approvals.length > 0)
                {
                    for (let approval of request.approvals)
                    {
                        addApprovalRow(approval, yourRequestExpansionApprovalsTableBody);
                    }

                    yourRequestExpansionApprovalsTable.removeAttribute('hidden');
                }
                else
                {
                    yourRequestExpansionApprovalsTable.hidden = true;
                }
            }
            else
            {
                viewMoreButton.innerText = "View Details";
                currentButtonHolder.currentButton = null;
                document.getElementById('your-request-expansion-div').hidden = 'true';
            }
        };

        viewMoreButton.onclick = toggleFunction;
        actionsCell.appendChild(viewMoreButton);

        tr.appendChild(submissionDateCell);
        tr.appendChild(eventDescriptionCell);
        tr.appendChild(eventDateCell);
        tr.appendChild(statusCell);
        tr.appendChild(actionsCell);

        yourRequestTableBody.appendChild(tr);
    }
}

async function refreshApprovableRequests(username, password)
{
    // fetch approvals
    const headers = new Headers();
    headers.set('Authorization', 'Basic ' + btoa(username + ":" + password));
    const url = `http://localhost:7000/requests/?type=approvable`;

    const requestsResponse = await fetch(url, {headers: headers});
    const requestsResponseBody = await requestsResponse.json();
    
    const approvableRequestTableBody = document.getElementById('approvable-requests-table-body');
    approvableRequestTableBody.innerHTML = "";

    // if no approvable requests pending, do nothing
    if (!requestsResponseBody.length)
    {
        approvableRequestsDiv.hidden = 'true';
        return;
    }
    // display approval headers and buttons
    // view more button in header will show/populate "view more" section and approval form

    approvableRequestsDiv.removeAttribute('hidden');

    let currentButtonHolder = {
        currentButton: null,
        currentRequest: null
    };

    for (let request of requestsResponseBody)
    {
        let tr = document.createElement('tr');

        let requestID = request.requestID;

        let submissionDate = timeStringToLocalDateString(request.submissionTime);
        let eventDate = timeStringToLocalDateString(request.eventTime);

        let eventDescription = request.eventDescription;
        let shortEventDescription = eventDescription.length > 40 ? eventDescription.substring(0, 40) : eventDescription;

        let requestorName = request.employeeName;
        let requestorNameCell = document.createElement('td');
        requestorNameCell.innerText = requestorName;

        let submissionDateCell = document.createElement('td');
        submissionDateCell.innerText = submissionDate;

        let eventDescriptionCell = document.createElement('td');
        eventDescriptionCell.innerText = shortEventDescription;

        let eventDateCell = document.createElement('td');
        eventDateCell.innerText = eventDate;

        let statusCell = document.createElement('td');
        let status = request.status.replace(/_/g, ' ');
        statusCell.innerText = status;

        let actionsCell = document.createElement('td');
        let viewMoreButton = document.createElement('button');
        viewMoreButton.innerText = "View Details";

        let canBeAwarded = request.status == "NEEDS_PROOF_OF_COMPLETION";

        let toggleFunction = ()=>
        {
            if (viewMoreButton.innerText == "View Details")
            {
                viewMoreButton.innerText = "Close";
                if (currentButtonHolder.currentButton != null && currentButtonHolder.currentRequest != null)
                {
                    currentButtonHolder.currentButton.onclick();
                }
                currentButtonHolder.currentButton = viewMoreButton;
                currentButtonHolder.currentRequest = request;
                // populate request expansion
                if (canBeAwarded)
                {
                    awardSubmissionDiv.removeAttribute('hidden');
                    awardSubmissionButton.innerText = 'Award Request';
                    awardSubmissionButton.removeAttribute('disabled');
                }
                else
                {
                    submitApprovalButton.innerText = 'Submit Approval';
                    submitApprovalButton.removeAttribute('disabled');
                    approvalSubmissionDiv.removeAttribute('hidden');
                }

                let requestExpansionDiv = document.getElementById('approvable-request-expansion-div');
                requestExpansionDiv.removeAttribute('hidden');

                document.getElementById('approvable-request-expansion-employee').innerText = requestorName;
                document.getElementById('approvable-request-expansion-submission-date').innerText = submissionDate;
                document.getElementById('approvable-request-expansion-event-description').innerText = (eventDescription);
                document.getElementById('approvable-request-expansion-event-date').innerText = (eventDate);
                document.getElementById('approvable-request-expansion-status').innerText = (status);
                document.getElementById('approvable-request-expansion-event-type').innerText = (request.eventTypeName);
                document.getElementById('approvable-request-expansion-event-cost').innerText = (request.eventCost);
                document.getElementById('approvable-request-expansion-reimbursement-amount').innerText = (request.reimbursementAmount);
                document.getElementById('approvable-request-expansion-grading-format').innerText = (request.gradingFormatName)
                document.getElementById('approvable-request-expansion-passing-grade').innerText = (request.passingGrade);
                document.getElementById('approvable-request-expansion-work-related-justification').innerText = (request.workRelatedJustification);
                document.getElementById('approvable-request-expansion-work-time-missed').innerText = (request.workTimeMissed);

                // populate approvals for the expanded request
                approvableRequestExpansionApprovalsTableBody.innerHTML = "";
                if (request.approvals && request.approvals.length > 0)
                {
                    for (let approval of request.approvals)
                    {
                        addApprovalRow(approval, approvableRequestExpansionApprovalsTableBody);
                    }
                    
                    approvableRequestExpansionApprovalsTable.removeAttribute('hidden');
                }
                else
                {
                    approvableRequestExpansionApprovalsTable.hidden = true;
                }
                
                submitApprovalButton.onclick = async ()=>
                {
                    submitApprovalButton.innerText = 'Please wait...';
                    submitApprovalButton.disabled = true;

                    let requestID = currentButtonHolder.currentRequest.requestID;
                    let approved = document.querySelector('input[name="approved-denied-radio"]:checked').value == 'approved';
                    let comments = approvalSubmissionComments.value;

                    let requestBody = JSON.stringify({
                        requestID: requestID,
                        approved: approved,
                        comments: comments
                    });

                    let headers = new Headers();
                    headers.set('Authorization', 'Basic ' + btoa(username + ":" + password));
                    const url = `http://localhost:7000/approvals`;

                    const approvalResponse = await fetch(url, {method: "POST", headers: headers, body: requestBody});
                    const approvalResponseBody = await approvalResponse.json();

                    currentButtonHolder.currentButton.onclick();

                    if (approvalResponse.ok)
                    {
                        statusDiv.innerHTML = "<p>Approval submission successful</p>"
                    }
                    else
                    {
                        statusDiv.innerHTML = "<p>Failed to submit approval</p>"
                    }

                    refreshApprovableRequests(username, password);
                };
                
                awardSubmissionButton.onclick = async ()=>
                {
                    awardSubmissionButton.innerText = 'Please wait...';
                    awardSubmissionButton.disabled = true;

                    let requestBody = JSON.stringify({
                        award: true
                    });

                    let headers = new Headers();
                    headers.set('Authorization', 'Basic ' + btoa(username + ":" + password));
                    const url = `http://localhost:7000/requests/` + requestID;

                    const awardResponse = await fetch(url, {method: "PATCH", headers: headers, body: requestBody});

                    currentButtonHolder.currentButton.onclick();

                    if (awardResponse.ok)
                    {
                        statusDiv.innerHTML = "<p>Award awarded successfully</p>"
                    }
                    else
                    {
                        statusDiv.innerHTML = "<p>Failed to submit award</p>"
                    }

                    refreshApprovableRequests(username, password);
                };
            }
            else
            {
                viewMoreButton.innerText = "View Details";
                currentButtonHolder.currentButton = null;
                currentButtonHolder.currentRequest = null;
                document.getElementById('approvable-request-expansion-div').hidden = 'true';
                approvalSubmissionDiv.hidden = true;
                awardSubmissionDiv.hidden = true;
                submitApprovalButton.disabled = true;
                awardSubmissionButton.disabled = true;
            }
        };

        viewMoreButton.onclick = toggleFunction;
        actionsCell.appendChild(viewMoreButton);

        tr.appendChild(requestorNameCell);
        tr.appendChild(submissionDateCell);
        tr.appendChild(eventDescriptionCell);
        tr.appendChild(eventDateCell);
        tr.appendChild(statusCell);
        tr.appendChild(actionsCell);

        approvableRequestTableBody.appendChild(tr);
    }


}

async function initNewRequestForm(username, password)
{
    initRequestButton.innerText = "Please wait...";

    const headers = new Headers();
    headers.set('Authorization', 'Basic ' + btoa(username + ":" + password));
    const url = `http://localhost:7000/requests/new`;

    const requestInfoResponse = await fetch(url, {headers: headers});
    const requestInfoBody = await requestInfoResponse.json();

    initRequestButton.innerText = 'Cancel request submission';
    initRequestButton.onclick = ()=>{
        initRequestButton.innerText = 'Create new reimbursement request?'
        initRequestButton.onclick = ()=>initNewRequestForm(username, password);
        newRequestFormDiv.hidden = 'true';
    };

    let isRequestValid = ()=>
    {
        if (!passingGradeInput.value
            || !eventDateInput.value
            || !eventAddressInput.value
            || !eventCostInput.value
            || eventCostInput.value <= 0
            || !eventDescriptionInput.value
            || !workRelatedJustificationInput.value)
        {
            return false;
        }

        return true;
    };

    let validateRequest = ()=>
    {
        if (isRequestValid())
        {
            submitNewRequestButton.removeAttribute('disabled');
        }
        else
        {
            submitNewRequestButton.disabled = 'true';
        }
    };

    for (let element of newRequestInputs)
    {
        element.removeAttribute('disabled');
    }
    validateRequest();
    newRequestFormDiv.removeAttribute('hidden');

    const remainingReimbursementCents = requestInfoBody.remainingReimbursement;
    const eventTypes = requestInfoBody.eventTypes;
    const gradingFormats = requestInfoBody.gradingFormats;
    const remainingReimbursementText = centsToDollarsAndCents(remainingReimbursementCents);
    document.getElementById('remaining-reimbursement-display').value = remainingReimbursementText;

    const projectedReimbursementDisplay = document.getElementById('projected-reimbursement-display');
    projectedReimbursementDisplay.value = '0.00';

    // get map of results keyed by id
    const eventTypeMap = mapBy(eventTypes, eventType => eventType.eventTypeID);
    const gradingFormatMap = mapBy(gradingFormats, format => format.formatID);

    eventTypeDropdown.innerHTML = "";
    gradingFormatDropdown.innerHTML = "";

    for(let eventType of eventTypes)
    {
        let option = document.createElement('option');
        option.value = eventType.eventTypeID;
        option.innerText = eventType.name;
        eventTypeDropdown.appendChild(option);
    }
    for(let gradingFormat of gradingFormats)
    {
        let option = document.createElement('option');
        option.value = gradingFormat.formatID;
        option.innerText = gradingFormat.name;
        gradingFormatDropdown.appendChild(option);
    }

    let updateProjectedReimbursement = ()=>
    {
        let eventCostInputValue = eventCostInput.value;
        let eventCostCents = dollarsAndCentsStringToCents(eventCostInputValue);
        let eventTypeID = parseInt(eventTypeDropdown.value,10);
        let eventType = eventTypeMap.get(eventTypeID);
        if (!eventType)
        {
            projectedReimbursementDisplay.value = '0.00';
            return;
        }
        
        let coveragePercentage = eventType.coveragePercentage;
        let coverableCost = Math.floor((eventCostCents * coveragePercentage) / 100);
        let coveredCost = Math.min(coverableCost, remainingReimbursementCents);
        let coveredCostText = centsToDollarsAndCents(coveredCost);
        projectedReimbursementDisplay.value = coveredCostText;
        validateRequest();
    };

    for (let input of newRequestInputs)
    {
        input.onchange = validateRequest;
        input.onkeyup = validateRequest;
    }

    eventTypeDropdown.onchange = updateProjectedReimbursement;
    eventCostInput.onkeyup = updateProjectedReimbursement;
    eventCostInput.onchange = updateProjectedReimbursement;

    gradingFormatDropdown.onchange = ()=>
    {
        let formatID = parseInt(gradingFormatDropdown.value, 10);
        let gradingFormat = gradingFormatMap.get(formatID);
        if (gradingFormat)
        {
            let defaultPassingGrade = gradingFormat.defaultPassingGrade;
            passingGradeInput.value = defaultPassingGrade;
        }
        validateRequest();
    };

    submitNewRequestButton.onclick = async function()
    {
        let requestInputs = document.getElementsByClassName('new-request-input');
        for (let element of requestInputs)
        {
            element.disabled = 'true';
        }
        submitNewRequestButton.innerText = "Please wait...";
        let eventType = parseInt(eventTypeDropdown.value, 10);
        let eventDate = String(new Date(eventDateInput.value).getTime());
        let gradingFormat = parseInt(gradingFormatDropdown.value,10);
        let requestBody = JSON.stringify({
            eventType: eventType,
            eventDate: eventDate,
            eventAddress: eventAddressInput.value,
            eventDescription: eventDescriptionInput.value,
            eventCost: eventCostInput.value,
            gradingFormat: gradingFormat,
            workRelatedJustification: workRelatedJustificationInput.value,
            passingGrade: passingGradeInput.value,
            workTimeMissed: workTimeMissedInput.value
        });
        
        postURL = "http://localhost:7000/requests";
        const headers = new Headers();
        headers.set('Authorization', 'Basic ' + btoa(username + ":" + password));
        const newRequestResponse = await fetch(postURL, {method: "POST", headers: headers, body: requestBody});
        const newRequestResponseBody = await newRequestResponse.json();

        newRequestFormDiv.hidden='true';
        submitNewRequestButton.innerText = 'Submit Reimbursement Request';
        initRequestButton.innerText = 'Create new reimbursement request?';
        initRequestButton.onclick = ()=>initNewRequestForm(username,password);
        statusDiv.innerHTML = "<p>New request posted successfully, refreshing requests...</p>"

        refreshRequests(username, password);

        if (newRequestResponse.ok)
        {
            statusDiv.innerHTML = "<p>New request posted successfully.</p>"
        }
        else
        {
            statusDiv.innerHTML = "<p id='bad-request-post'>Failed to submit new request.</p>";
        }
    };
}

function timeStringToLocalDateString(epochMilliString)
{
    if (epochMilliString == null)
        return "--";
    
    let millis = Number(epochMilliString);
    let date = new Date(millis);
    let formattedDate = "" + date.getFullYear() + "/" + (date.getMonth()+1) + "/" + date.getDate();
    return formattedDate;
}

/**
 * Converts a list of things to a map of things
 * @param {array} things Array of things
 * @param {function} getter thing=>key function
 * @returns {Map}
 */
function mapBy(things, getter)
{
    let result = new Map();
    for (let thing of things)
    {
        result.set(getter(thing), thing);
    }
    return result;
}

/**
 * @param {int} totalCents 
 * @returns {string}
 */
function centsToDollarsAndCents(totalCents)
{
    if (totalCents > 0)
    {
        let dollars = Math.floor(totalCents/100);
        let cents = totalCents % 100;
        cents = cents < 10 ? "0" + cents : cents;
        return "" + dollars + "." + cents;
    }
    else
    {
        return "0.00";
    }
}

/**
 * 
 * @param {string} dsString 
 * @returns {int}
 */
function dollarsAndCentsStringToCents(dsString)
{
    let dollarsAndCents = dsString.split('.');
    if (dollarsAndCents.length > 2 || dollarsAndCents.length == 0)
    {
        return 0;
    }
    let cents = dollarsAndCents.length >= 2
        ? dollarsAndCents[1].length > 2
            ? 0
            : parseInt(dollarsAndCents[1], 10)
        : 0;
    let dollars = parseInt(dollarsAndCents[0], 10);
    return dollars*100 + cents;
}

/**
 * Adds approval data to a new row in a given table body
 */
function addApprovalRow(approval, tableBody)
{
    let tr = document.createElement('tr');

    let appendData = data=>{
        let td = document.createElement('td');
        td.innerText = data;
        tr.appendChild(td);
    };
    
    appendData(approval.approver);
    appendData(approval.approvalLevel);
    appendData(approval.approved ? 'APPROVED' : 'DENIED');
    appendData(timeStringToLocalDateString(String(approval.approvalDate)));
    appendData(approval.comments);

    tableBody.appendChild(tr);
}