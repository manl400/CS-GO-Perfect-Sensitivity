const LOW_MODS = [0, 0.5, 0.8, 0.8, 0.8, 0.95];
const HIGH_MODS = [0, 1.5, 1.2, 1.2, 1.2, 1.05];

const state = {
  avg: 0,
  low: 0,
  high: 0,
  tests: 0,
  timesRan: 1,
  started: false,
};

const avgInput = document.querySelector('#avg-input');
const startBtn = document.querySelector('#start-btn');
const resetBtn = document.querySelector('#reset-btn');
const lowField = document.querySelector('#low-field');
const highField = document.querySelector('#high-field');
const avgField = document.querySelector('#avg-field');
const copyButtons = document.querySelectorAll('.copy-btn');
const modifierValue = document.querySelector('#modifier-value');
const choiceFieldset = document.querySelector('.selection');
const continueBtn = document.querySelector('#continue-btn');
const finishBtn = document.querySelector('#finish-btn');
const statusLabel = document.querySelector('#status');

startBtn.addEventListener('click', onStart);
resetBtn.addEventListener('click', reset);
continueBtn.addEventListener('click', onContinue);
finishBtn.addEventListener('click', onFinish);
copyButtons.forEach((btn) => btn.addEventListener('click', onCopy));
choiceFieldset.addEventListener('change', onChoiceChange);

function onStart() {
  const value = Number.parseFloat(avgInput.value);
  if (Number.isNaN(value) || value <= 0) {
    showStatus('Please enter a positive number to begin.', true);
    avgInput.focus();
    return;
  }

  state.avg = value;
  state.low = value * LOW_MODS[1];
  state.high = value * HIGH_MODS[1];
  state.tests = 1;
  state.timesRan = 1;
  state.started = true;

  renderFields();
  updateControls({ started: true, running: true });
  showStatus('Round 1: Test the suggested low and high sensitivities.');
}

function onContinue() {
  const choice = getChoice();
  if (!choice) {
    showStatus('Select whether the low or high option felt better before continuing.', true);
    return;
  }

  state.timesRan += 1;
  if (state.tests < 5) {
    state.tests += 1;
  }

  if (choice === 'low') {
    applyLowChoice();
  } else {
    applyHighChoice();
  }

  renderFields();
  choiceFieldset.reset();
  modifierValue.textContent = '—';
  showStatus(`Round ${state.timesRan}: Keep refining!`);
}

function onFinish() {
  if (!state.started) {
    showStatus('Start the process before finishing.', true);
    return;
  }

  const formattedAvg = formatNumber(state.avg);
  const message = `sensitivity ${formattedAvg}`;

  copyToClipboard(message)
    .then(() => showToast(`Copied “${message}”`))
    .catch(() => showStatus('Unable to access clipboard. Copy manually if needed.', true));

  showStatus(`All done! It took ${state.timesRan} tries. The average has been copied to your clipboard.`);
  updateControls({ started: true, running: false });
}

function onCopy(event) {
  const target = document.querySelector(`#${event.currentTarget.dataset.target}`);
  if (!target.value) {
    return;
  }

  const command = `sensitivity ${target.value}`;
  copyToClipboard(command)
    .then(() => showToast(`Copied “${command}”`))
    .catch(() => showStatus('Unable to access clipboard. Copy manually if needed.', true));
}

function onChoiceChange(event) {
  if (!event.target.matches('input[type="radio"]')) {
    return;
  }
  const value = event.target.value;
  const mods = value === 'low' ? LOW_MODS : HIGH_MODS;
  modifierValue.textContent = state.tests > 0 ? formatNumber(mods[state.tests]) : '—';
}

function applyLowChoice() {
  if (state.tests >= 2 && state.tests <= 4) {
    const newLow = state.low * LOW_MODS[2];
    state.high = state.avg;
    state.avg = (state.avg + newLow) / 2;
    state.low = newLow;
  } else if (state.tests === 5) {
    const previousLow = state.low;
    const newLow = state.low * LOW_MODS[5];
    state.high = state.avg;
    state.avg = (previousLow + newLow) / 2;
    state.low = newLow;
  }
}

function applyHighChoice() {
  if (state.tests >= 2 && state.tests <= 4) {
    const newHigh = state.high * HIGH_MODS[2];
    state.low = state.avg;
    state.avg = (state.avg + newHigh) / 2;
    state.high = newHigh;
  } else if (state.tests === 5) {
    const newHigh = state.high * HIGH_MODS[5];
    state.low = state.avg;
    state.avg = (state.avg + newHigh) / 2;
    state.high = newHigh;
  }
}

function renderFields() {
  if (!state.started) {
    lowField.value = '';
    highField.value = '';
    avgField.value = '';
    return;
  }

  lowField.value = formatNumber(state.low);
  highField.value = formatNumber(state.high);
  avgField.value = formatNumber(state.avg);
}

function updateControls({ started, running }) {
  startBtn.disabled = started;
  avgInput.disabled = started;
  resetBtn.disabled = !started;
  continueBtn.disabled = !running;
  finishBtn.disabled = !running;
  choiceFieldset.disabled = !running;
  copyButtons.forEach((btn) => {
    btn.disabled = !started;
  });

  if (!running) {
    choiceFieldset.reset();
    modifierValue.textContent = '—';
  }
}

function reset() {
  Object.assign(state, {
    avg: 0,
    low: 0,
    high: 0,
    tests: 0,
    timesRan: 1,
    started: false,
  });

  avgInput.value = '';
  modifierValue.textContent = '—';
  renderFields();
  updateControls({ started: false, running: false });
  showStatus('Waiting to start.');
}

function getChoice() {
  const checked = choiceFieldset.querySelector('input[name="choice"]:checked');
  return checked ? checked.value : null;
}

function formatNumber(value) {
  if (!Number.isFinite(value)) {
    return '';
  }
  return Number.parseFloat(value.toFixed(5)).toString();
}

function showStatus(message, isError = false) {
  statusLabel.textContent = `Status: ${message}`;
  statusLabel.classList.toggle('error', isError);
}

function showToast(message) {
  const template = document.querySelector('#toast-template');
  const toast = template.content.firstElementChild.cloneNode(true);
  toast.textContent = message;
  document.body.appendChild(toast);
  setTimeout(() => toast.remove(), 2500);
}

async function copyToClipboard(text) {
  if (navigator.clipboard?.writeText) {
    return navigator.clipboard.writeText(text);
  }

  const textarea = document.createElement('textarea');
  textarea.value = text;
  textarea.setAttribute('readonly', '');
  textarea.style.position = 'absolute';
  textarea.style.left = '-9999px';
  document.body.appendChild(textarea);
  textarea.select();
  document.execCommand('copy');
  document.body.removeChild(textarea);
  return Promise.resolve();
}

reset();
