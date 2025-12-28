import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { connect } from 'react-redux';
import '../assets/styles/th-bank.css';

import Dialog from '../components/Dialog';
import HeaderAct from '../components/HeaderAct';
import commonUtils from '../utils/CommonUtils';
import alertUtils from '../utils/AlertUtils';

import { setupMainAccountApi } from '../services/AccountService';

const SetMainAccount = (props) => {
    const navigate = useNavigate();
    const { user, accounts, deleteUserData } = props;
    
    const [selectedId, setSelectedId] = useState('');
    const [dialogData, setDialogData] = useState(alertUtils.close());

    useEffect(() => {
        if (!user || !user.userId) {
            setDialogData(
                alertUtils.alert({
                title: 'ระบบแจ้งเตือน',
                message: 'กรุณาเข้าสู่ระบบอีกครั้ง',
                onClose: () => {
                    deleteUserData();
                    navigate('/login');
                },
                })
            );
            return;
        }

        const currentMain = accounts?.find((a) => a.isMainAccount);
        setSelectedId(currentMain?.accountId || accounts?.[0]?.accountId || '');
    }, [user, accounts]);

    const handleSave = async () => {
        if (!user || !user.userId || !selectedId || !accounts || accounts.length === 0) return;

        const currentMain = accounts.find((a) => a.isMainAccount)?.accountId;
        if (currentMain === selectedId) {
            navigate(-1);
            return;
        }

        try {
            const res = await setupMainAccountApi(user.userId, selectedId);
            if (res && res.status === 200) {
                navigate('/bank');
            } else {
                throw new Error('Failed to set main account');
            }
        } catch (error) {
            setDialogData(
                alertUtils.alert({
                    title: 'เกิดข้อผิดพลาด',
                    message: error.response?.data?.message || 'ไม่สามารถบันทึกได้ กรุณาลองใหม่อีกครั้ง',
                    onClose: () => setDialogData(alertUtils.close()),
                })
            );
        }
    };

    return (
        <div className="wrap">
            <HeaderAct />

            <main className="container container--main">
                <div className="content_wrap">
                    <div className="main-top" style={{ marginBottom: '16px' }}>
                        <h1 className="main-top__tit">Set Main Account</h1>
                    </div>

                    {accounts && accounts.length > 0 ? (
                        <div>
                        {accounts.map((acc) => (
                            <label key={acc.accountId} className="main-acc is-small" style={{background: acc.color || '', color: commonUtils.getContrastYIQ(acc.color || '#ffffff'), cursor: 'pointer', display: 'flex', alignItems: 'center', position: 'relative'}}>
                            <input
                                type="radio"
                                name="main-account"
                                value={acc.accountId}
                                checked={selectedId === acc.accountId}
                                onChange={() => setSelectedId(acc.accountId)}
                                style={{ position: 'absolute', left: '12px', cursor: 'pointer' }}
                            />
                            <div style={{ marginLeft: '40px', width: '100%' }}>
                                <div className="main-acc__top">
                                <h2 className="main-acc__name">{acc.nickname || 'Saving Account'}</h2>
                                <span className="main-acc__amount">{commonUtils.currencySign(acc.currency)} {(Number(acc.amount) || 0).toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}</span>
                                {acc.flags && acc.flags.length > 0 && acc.flags.map((flag) => <span key={flag.flagValue} className="main-acc__flag">{flag.flagValue}</span>)}
                                </div>
                                <div className="main-acc__bottom">
                                <span className="main-acc__detail">{`${acc.type || ''} ${acc.accountNumber || ''}`}</span>
                                {acc.issuer && <span className="main-acc__detail">{`Powered by ${acc.issuer}`}</span>}
                                </div>
                            </div>
                            </label>
                        ))}
                        </div>
                    ) : (
                        <p style={{ padding: '16px' }}>No accounts available.</p>
                    )}
                </div>
            </main>

            <button type="button"
                onClick={handleSave}
                style={{ position: 'fixed', bottom: 0, left: 0, right: 0, width: '100%', padding: '12px 24px', background: '#24c875', color: '#fff', border: 'none', borderRadius: 0, cursor: 'pointer' }}
            >
                Save
            </button>

            <Dialog
                isOpen={dialogData.isOpen}
                title={dialogData.title}
                message={dialogData.message}
                confirmLabel={dialogData.confirmLabel}
                buttons={dialogData.buttons}
                onClose={() => setDialogData(alertUtils.close())}
            />
        </div>
    );
};

const mapStateToProps = (state) => ({
    user: state.user.user,
    accounts: state.account.accounts || [],
});

const mapDispatchToProps = (dispatch) => ({
    async deleteUserData() {
        dispatch({ type: 'USER_LOGOUT' });
    },
});

export default connect(mapStateToProps, mapDispatchToProps)(SetMainAccount);
