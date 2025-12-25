const nofunction = () => {};

const DEFAULT_DIALOG_STATE = {
    isOpen: false,
    title: 'ระบบแจ้งเตือน',
    message: '',
    confirmLabel: 'ตกลง',
    buttons: [],
    onClose: nofunction,
};

const createBaseState = () => ({
    ...DEFAULT_DIALOG_STATE,
    buttons: [],
    onClose: nofunction,
});

const buildState = (overrides = {}) => ({
    ...createBaseState(),
    ...overrides,
});

const normalizeButtons = (buttons = []) =>
    buttons.map((button, index) => ({
        buttonLabel: button.buttonLabel || `ตัวเลือก ${index + 1}`,
        onClick: button.onClick,
        color: button.color || 'primary',
        shouldClose: button.shouldClose !== undefined ? button.shouldClose : true,
    }));

const alert = ({ title, message, confirmLabel, onClose } = {}) =>
    buildState({
        isOpen: true,
        title: title ?? DEFAULT_DIALOG_STATE.title,
        message: message ?? DEFAULT_DIALOG_STATE.message,
        confirmLabel: confirmLabel ?? DEFAULT_DIALOG_STATE.confirmLabel,
        onClose: onClose ?? nofunction,
        buttons: [],
    });

const confirm = ({ title, message, buttons = [], onClose } = {}) => {
    const normalizedButtons = normalizeButtons(buttons);
    return buildState({
        isOpen: true,
        title: title ?? DEFAULT_DIALOG_STATE.title,
        message: message ?? DEFAULT_DIALOG_STATE.message,
        onClose: onClose ?? nofunction,
        buttons:
            normalizedButtons.length > 0
                ? normalizedButtons
                : [
                      {
                          buttonLabel: DEFAULT_DIALOG_STATE.confirmLabel,
                          onClick: undefined,
                          color: 'primary',
                          shouldClose: true,
                      },
                  ],
    });
};

const close = () => buildState();

const alertUtils = {
    alert,
    confirm,
    close,
    defaults: DEFAULT_DIALOG_STATE,
};

export default alertUtils;
export { DEFAULT_DIALOG_STATE };
