import { useEffect } from 'react';

import '../assets/styles/dialog.css';

const Dialog = ({
    isOpen = false,
    title = 'ระบบแจ้งเตือน',
    message = '',
    confirmLabel = 'ตกลง',
    buttons = [],
    onClose = () => {},
}) => {
    useEffect(() => {
        if (!isOpen) {
            return undefined;
        }

        const handleKeyDown = (event) => {
            if (event.key === 'Escape') {
                onClose();
            }
        };

        document.body.style.overflow = 'hidden';
        window.addEventListener('keydown', handleKeyDown);

        return () => {
            document.body.style.overflow = '';
            window.removeEventListener('keydown', handleKeyDown);
        };
    }, [isOpen, onClose]);

    if (!isOpen) {
        return null;
    }

    const dialogButtons =
        Array.isArray(buttons) && buttons.length > 0
            ? buttons
            : [
                  {
                      buttonLabel: confirmLabel,
                      color: 'primary',
                      shouldClose: true,
                  },
              ];

    const handleButtonAction = (buttonAction = {}) => {
        const { onClick, shouldClose = true } = buttonAction;
        if (typeof onClick === 'function') {
            onClick();
        }
        if (shouldClose) {
            onClose();
        }
    };

    return (
        <div className="dialog-backdrop" role="dialog" aria-modal="true" aria-labelledby="app-dialog-title">
            <div className="dialog-panel">
                {title ? (
                    <h2 id="app-dialog-title" className="dialog-title">
                        {title}
                    </h2>
                ) : null}
                <p className="dialog-message">{message}</p>
                <div className="dialog-actions">
                    {dialogButtons.map((button, index) => {
                        const { buttonLabel, color = 'primary' } = button;
                        const colorClass = `dialog-button dialog-button--${color}`;
                        return (
                            <button
                                type="button"
                                key={`dialog-button-${index}`}
                                className={colorClass}
                                onClick={() => handleButtonAction(button)}
                            >
                                {buttonLabel || confirmLabel}
                            </button>
                        );
                    })}
                </div>
            </div>
        </div>
    );
};

export default Dialog;
