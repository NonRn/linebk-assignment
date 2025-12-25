import { useState, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { connect } from 'react-redux';
import '../assets/styles/th-bank.css';

import { authenticatePasscodeUserApi } from '../services/UserService';
import Dialog from '../components/Dialog';
import alertUtils from '../utils/AlertUtils';

const MAX_LENGTH = 6;
const KEYS = ['1', '2', '3', '4', '5', '6', '7', '8', '9', 'space', '0', 'delete'];

const Pin = (props) => {
    const navigate = useNavigate();
    const { user } = props;
    const [pinValue, setPinValue] = useState('');
    const [dialogData, setDialogData] = useState(alertUtils.close());

    const handleSubmit = (key) => {
        if(user && user.userId && key){
            authenticatePasscodeUserApi(user.userId, key)
                .then(response => {
                    if (response.status === 200) {
                        navigate('/bank');
                    } else {
                        // Handle authentication failure
                    }
                })
                .catch(error => {
                    console.error('Authentication failed:', error);
                    setDialogData(
                        alertUtils.alert({
                            title: 'ระบบแจ้งเตือน',
                            message: 'เกิดข้อผิดพลาด กรุณาลองใหม่อีกครั้ง',
                            onClose: () => {
                                setPinValue('');
                            },
                        }),
                    );
                });
        }
    }

    const handleKeyPress = (key) => {
        if (key === 'space') {
            return;
        }

        if (key === 'delete') {
            setPinValue((prev) => prev.slice(0, -1));
            return;
        }

        setPinValue((prev) => {
            if (prev.length >= MAX_LENGTH) {
                return prev;
            }

            const next = `${prev}${key}`;

            if (next.length === MAX_LENGTH) {
                handleSubmit(next);
            }

            return next;
        });
    }

    const handleCredentialNavigation = useCallback(() => {
        navigate('/login');
    }, [navigate]);

    const handleDialogClose = (callback) => {
        setDialogData(alertUtils.close());
        if (callback) {
            callback();
        }
    };

    return (
        <div className="wrap" style={{ minHeight: '100vh' }}>
        <main className="container container--pin-type">
            <div className="pin">
            <div className="pin__top">
                <span className="pin__photo">
                <img src={user && user.profileImage ? user.profileImage : "https://dummyimage.com/200x200/999/fff"} alt="" />
                </span>
                <h1 className="pin__name">{user && user.name ? user.name : 'Interview User'}</h1>
                <p className="pin__dsc" style={{ display: 'none' }}>
                Invalid PIN Code.
                <br />
                You have 3 attempt left.
                </p>
                <div className="pin__dots">
                {Array.from({ length: MAX_LENGTH }).map((_, idx) => (
                    <span
                    key={`pin-dot-${idx}`}
                    className={`pin__dot${idx < pinValue.length ? ' is-filled' : ''}`}
                    />
                ))}
                </div>
            </div>
            <div className="pin__btm">
                <button type="button" className="pin__login" onClick={handleCredentialNavigation}>
                Login with ID / Password
                </button>
                <span className="pin__kb">Powered by TestLab</span>
                <div className="pin__keys">
                {KEYS.map((key) => {
                    if (key === 'space') {
                    return <span key="pin-key-space" className="pin__key pin__key--space" aria-hidden="true" />;
                    }

                    if (key === 'delete') {
                    return (
                        <button
                        key="pin-key-delete"
                        type="button"
                        className="pin__key pin__key--del"
                        onClick={() => handleKeyPress(key)}
                        >
                        &larr;
                        </button>
                    );
                    }

                    return (
                    <button
                        key={`pin-key-${key}`}
                        type="button"
                        className="pin__key"
                        onClick={() => handleKeyPress(key)}
                    >
                        {key}
                    </button>
                    );
                })}
                </div>
            </div>
            </div>
        </main>
        <Dialog
            isOpen={dialogData.isOpen}
            title={dialogData.title}
            message={dialogData.message}
            confirmLabel={dialogData.confirmLabel}
            buttons={dialogData.buttons}
            onClose={() => handleDialogClose(dialogData.onClose)}
        />
        </div>
    );
};

const mapStateToProps = (state) => {
    const { user } = state.user ;
    return {
        user,
    };
}

const mapDispatchToProps = (dispatch) => ({

});

export default connect(mapStateToProps, mapDispatchToProps)(Pin);
