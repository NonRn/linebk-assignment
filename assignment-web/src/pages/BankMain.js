import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { connect } from 'react-redux';
import '../assets/styles/th-bank.css';
import Dialog from '../components/Dialog';

import commonUtils from '../utils/CommonUtils';
import alertUtils from '../utils/AlertUtils';

import { getBannerByUserIdApi } from '../services/BannerService';
import { getAccountByUserIdApi, withdrawApi } from '../services/AccountService';
import { getDebitCardByUserIdApi } from '../services/DebitCardService';
import { getTransactionByUserIdApi } from '../services/TransactionService';
import Splash from '../components/Splash';

const BankMain = (props) => {
    const { user, banners, accounts, debitCards, transactions, isLoading } = props; // redux state
    const { deleteUserData, fetchBanners, fetchAccounts, fetchDebitCards, fetchTransactions } = props; // dispatch function
    const navigate = useNavigate();

    const [dialogData, setDialogData] = useState(alertUtils.close());
    const [withDrawDialog, setWithDrawDialog] = useState(alertUtils.close());
    const [mainBankAccount, setMainBankAccount] = useState(null);
    const [snackbar, setSnackbar] = useState({ show: false, message: '' });
    const [activeTooltip, setActiveTooltip] = useState(null);

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
                }),
            );
            return;
        } else {
            fetchBanners(user.userId);
            fetchAccounts(user.userId);
            fetchDebitCards(user.userId);
            fetchTransactions(user.userId, 10, 0);
        }

        setDialogData(alertUtils.close());
    }, []);

    useEffect(() => {
        if (accounts && accounts.length > 0) {
            const mainAccount = accounts.find(account => account.isMainAccount);
            setMainBankAccount(mainAccount || null);
        }
    }, [accounts]);

    const handleCancel = () => {
        deleteUserData();
        navigate('/');
    };

    const handleDialogClose = (callback) => {
        setDialogData(alertUtils.close());
        if (callback) {
            callback();
        }
    };

    const handleMoreClick = (e, tooltipId) => {
        e.preventDefault();
        setActiveTooltip(activeTooltip === tooltipId ? null : tooltipId);
    };

    const handleCloseTooltip = (e) => {
        if (!e.target.closest('.main-acc__more') && !e.target.closest('.tooltip')) {
            setActiveTooltip(null);
        }
    };

    const handleCopyAccountNumber = (accountNumber, e) => {
        if (accountNumber) {
            navigator.clipboard.writeText(accountNumber).then(() => {
                setActiveTooltip(null); // ปิด tooltip
                setSnackbar({ show: true, message: 'คัดลอกเลขที่บัญชีเรียบร้อย' });
                setTimeout(() => {
                    setSnackbar({ show: false, message: '' });
                }, 5000); // แสดง 5 วินาที
            }).catch(err => {
                console.error('Failed to copy account number:', err);
            });
        }
        if (e) {
            handleCloseTooltip(e);
        }
    };

    const handleQrScan = () => {
        navigate('/qrscan');
    }

    const handleAddMoney = () => {
        navigate('/addmoney');
    }

    const handleWithdrawal = (withdrawalAccount) => {
        if(!withdrawalAccount) return;

        setWithDrawDialog({
            isOpen: true,
            title: 'ถอนเงิน',
            message: (
                <div style={{ padding: '10px 0' }}>
                    <label style={{ display: 'block', marginBottom: '8px', fontSize: '14px' }}>
                        จำนวนเงินที่ต้องการถอน:
                    </label>
                    <input id='withdrawal-amount'
                        type="number"
                        placeholder="0.00"
                        style={{
                            width: '100%',
                            padding: '12px',
                            fontSize: '16px',
                            border: '1px solid #ddd',
                            borderRadius: '4px',
                            boxSizing: 'border-box'
                        }}
                        min="0"
                        step="0.1"
                    />
                </div>
            ),
            buttons: [
                {
                    buttonLabel: 'ยกเลิก',
                    color: 'secondary',
                    onClick: () => {
                        clearWithdrawalInput();
                        setWithDrawDialog(alertUtils.close());
                    }
                },
                {
                    buttonLabel: 'ยืนยัน',
                    onClick: () => {
                        handleWithdrawalSubmit(withdrawalAccount)
                    },
                }
            ]
        });
    }

    const clearWithdrawalInput = () => {
        const inputElement = document.getElementById('withdrawal-amount');
        if (inputElement) {
            inputElement.value = '';
        }
    }

    const handleWithdrawalSubmit = (withdrawalAccount) => {
        const inputElement = document.getElementById('withdrawal-amount');
        const withdrawalAmount = inputElement ? inputElement.value : '0';
        const amount = parseFloat(withdrawalAmount);
        
        if (!amount || amount <= 0) {
            setTimeout(() => {
                setDialogData(
                    alertUtils.alert({
                        title: 'ข้อผิดพลาด',
                        message: 'กรุณากรอกจำนวนเงินที่ถูกต้อง',
                        onClose: () => {
                            setDialogData(alertUtils.close());
                            handleWithdrawal(withdrawalAccount);
                        }
                    })
                );
            }, 100); // set timepout เพื่อรอให้ dialog ปิด และ ไม่ซ้อนกัน
            return;
        }

        if (withdrawalAccount && amount > withdrawalAccount.amount) {
            setTimeout(() => {
                setDialogData(
                    alertUtils.alert({
                        title: 'ข้อผิดพลาด',
                        message: 'จำนวนเงินไม่เพียงพอ',
                        onClose: () => {
                            setDialogData(alertUtils.close());
                            handleWithdrawal(withdrawalAccount);
                        }
                    })
                );
            }, 100); // set timepout เพื่อรอให้ dialog ปิด และ ไม่ซ้อนกัน
            return;
        }

        setTimeout(() => {
            setDialogData({
                isOpen: true,
                title: 'ยืนยันการถอนเงิน',
                message: (<p>คุณต้องการถอนเงิน จากบัญชี {withdrawalAccount.nickname ? withdrawalAccount.nickname : withdrawalAccount.accountNumber} <br/> จำนวน {commonUtils.currencySign(withdrawalAccount?.currency)}{amount.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })} ใช่หรือไม่?</p>),
                buttons: [
                    {
                        buttonLabel: 'ยกเลิก',
                        color: 'secondary',
                        onClick: () => {
                            setDialogData(alertUtils.close());
                            handleWithdrawal(withdrawalAccount);
                        }
                    },
                    {
                        buttonLabel: 'ยืนยัน',
                        onClick: () => handleWithdrawalConfirm(withdrawalAccount, amount)
                    }
                ]
            });
        }, 100);
    }

    const handleWithdrawalConfirm = async (withdrawalAccount, amount) => {
        try {
            setDialogData(alertUtils.close());
            clearWithdrawalInput();
            const response = await withdrawApi(withdrawalAccount.accountId, amount);
            
            if (response.status === 200) {
                console.log('Withdrawal successful:', response);
                setSnackbar({ show: true, message: 'ถอนเงินสำเร็จ' });
                setTimeout(() => {
                    setSnackbar({ show: false, message: '' });
                }, 5000);

                // Refresh account data
                if (user && user.userId) {
                    fetchAccounts(user.userId);
                }
            } else {
                throw new Error('Withdrawal failed');
            }
        } catch (error) {
            console.error('Withdrawal error:', error);
            setDialogData(
                alertUtils.alert({
                    title: 'เกิดข้อผิดพลาด',
                    message: error.response?.data?.message || 'ไม่สามารถถอนเงินได้ กรุณาลองใหม่อีกครั้ง',
                    onClose: () => {
                        setDialogData(alertUtils.close());
                    }
                })
            );
        }
    }

    return (
        <div className="wrap">
        {isLoading && <Splash isBlur={true}/>}
        <header className="header">
            <a href="#" className="header__lft header__menu" onClick={(event) => event.preventDefault()}>
            <span className="blind">Menu</span>
            </a>
            <button type="button" className="header__rgt header__cxl" onClick={handleCancel}>
            <span className="blind">Cancel</span>
            </button>
        </header>

        <main className="container container--main">
            <div className="content_wrap">
            <div className="main-top">
                <h1 className="main-top__tit main-loading main-loading--order1">{user && user.userId ? user.greetingText || `Have a nice day ${user.name || ''}` : ''}</h1>
            </div>

            {mainBankAccount && (
                <div className="main-acc main-acc--large main-loading main-loading--order3" style={{background: mainBankAccount.color || ''}}>
                    <div className="main-acc__top">
                        <h2 className="main-acc__name">{mainBankAccount.nickname || 'Saving Account'}</h2>
                        <span className="main-acc__amount">{commonUtils.currencySign(mainBankAccount.currency)} {mainBankAccount.amount ? mainBankAccount.amount.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 }) : '0.00'}</span>
                        <span className="main-acc__detail main-acc__detail--num">{`${mainBankAccount.type} ${mainBankAccount.accountNumber || ''}`}</span>
                        {mainBankAccount.issuer && <span className="main-acc__detail">{`Powered by ${mainBankAccount.issuer}`}</span>}
                    </div>

                    <div className="main-acc__bottom">
                    <div className="main-acc__ani_box">
                        <div className="main-acc__ani__item">
                        <span className="main-acc__ani_img1" />
                        <span className="main-acc__ani_img2" />
                        </div>
                        <div className="main-acc__ani__item2">
                        <span className="main-acc__ani_img3" />
                        </div>
                    </div>
                    <div className="main-acc__link__box">
                        <div className="main-acc__link__item">
                        <button className="main-acc__link main-acc__link--withdrawal" onClick={() => handleWithdrawal(mainBankAccount)}>
                            Withdrawal
                        </button>
                        <button className="main-acc__link main-acc__link--qr" onClick={handleQrScan}>
                            QR scan
                        </button>
                        <button className="main-acc__link main-acc__link--addmoney" onClick={handleAddMoney}>
                            Add money
                        </button>
                        </div>
                    </div>
                    </div>
                    <button type="button" className="main-acc__more" onClick={(e) => handleMoreClick(e, 'main')}>
                        <span className="blind">More Action</span>
                    </button>
                    <div className="tooltip" style={{ display: activeTooltip === 'main' ? 'block' : 'none' }}>
                        <button id="set-main-account" type="button" className="tooltip__btn-more" onMouseEnter={(e) => e.target.style.backgroundColor = '#e0e0e0'} onMouseLeave={(e) => e.target.style.backgroundColor = ''}>
                            Set main account
                        </button>
                        <button id="copy-account-number" type="button" className="tooltip__btn-more" onClick={(e) => handleCopyAccountNumber(mainBankAccount?.accountNumber, e)} onMouseEnter={(e) => e.target.style.backgroundColor = '#e0e0e0'} onMouseLeave={(e) => e.target.style.backgroundColor = ''}>
                            Copy account number
                        </button>
                        <button id="edit-name-color" type="button" className="tooltip__btn-more" onMouseEnter={(e) => e.target.style.backgroundColor = '#e0e0e0'} onMouseLeave={(e) => e.target.style.backgroundColor = ''}>
                            Edit Name and Color
                        </button>
                    </div>
                </div>
            )}

            <div className="rctly__wrap main-loading main-loading--order5">
                <ul className="rctly__lst">
                    {transactions && transactions.length > 0 && transactions.map((transaction, i) => (
                        i < 10 && //แสดง 10 รายการแรก
                        <li id={`transaction-${i}`} key={`transaction-${i}`} className="rctly__item">
                            <div className="rctly__link" onClick={(event) => event.preventDefault()}>
                                <span className={`rctly__thumb ${transaction.isBank ? 'is-bank' : ''}`}>
                                    <img src={transaction.image || "https://dummyimage.com/54x54/999/fff"} alt="" />
                                </span>
                                <span className="rctly__name">{transaction.name || ''}</span>
                            </div>
                        </li>
                    ))}
                </ul>
            </div>

            <div className="debit-swipe__wrap main-loading main-loading--order6">
                <div className="debit-swipe__inner">
                <div className="debit-swipe__lst" style={{ width: '1595px' }}>
                    {debitCards && debitCards.length > 0 && debitCards.map((card, i) => (
                        i < 5 && //แสดง 5 รายการแรก (ที่เหลือไปดูใน See all)
                        <span id={`card-${i}`}>
                            <div className="debit-swipe__item"
                            style={{ 
                                backgroundColor: card.color || '#00a1e2', 
                                borderColor: card.borderColor || '',
                                opacity: card.status === 'Active' ? 1 : 0.4,
                                color: card.status === 'Active' ? '' : '#000000'
                            }}
                            onClick={(event) => event.preventDefault()}
                            >
                                <strong className="debit-swipe__name">{card.name || 'Debit Card'}</strong>
                                <span className="debit-swipe__etc">{card.number ? commonUtils.maskingDebitCard(card.number, 6, 4) : "In progress"}</span>
                                <span className="debit-swipe__issue">{card.issuer ? `Issued by ${card.issuer}` : ""}</span>
                            </div>
                        </span>
                    ))}
                    {debitCards && debitCards.length > 5 &&
                        <a href="#" className="debit-swipe__item debit-swipe__item--all" onClick={(event) => event.preventDefault()}>
                        See all
                        </a>
                    }
                </div>
                </div>
            </div>

            {accounts && accounts.length > 0 && accounts.filter(account => !account.isMainAccount).map((account, idx) => (
                <div id={account.accountId} className="main-acc is-small" style={{background: account.color || '', cursor: 'pointer'}} onClick={() => handleWithdrawal(account)}>
                    <div className="main-acc__top">
                        <h2 className="main-acc__name">{account.nickname || 'Saving Account'}</h2>
                        <span className="main-acc__amount">{commonUtils.currencySign(account.currency)} {account.amount ? account.amount.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 }) : '0.00'}</span>
                        {account.flags && account.flags.length > 0 && account.flags.map((flag) => <span className="main-acc__flag">{flag.flagValue}</span>)}
                    </div>
                    <div className="main-acc__bottom">
                        <span className="main-acc__detail">{`${account.type} ${account.accountNumber || ''}`}</span>
                        {account.issuer && <span className="main-acc__detail">{`Powered by ${account.issuer}`}</span>}
                    </div>
                    <button type="button" className="main-acc__more main-acc__more--small" onClick={(e) => handleMoreClick(e, `acc-${idx}`)}>
                        <span className="blind">More Action</span>
                    </button>
                    <div className="tooltip tooltip--sub-acc" style={{ display: activeTooltip === `acc-${idx}` ? 'block' : 'none' }}>
                        <button type="button" className="tooltip__btn-more" onClick={() => handleCopyAccountNumber(account?.accountNumber)} onMouseEnter={(e) => e.target.style.backgroundColor = '#e0e0e0'} onMouseLeave={(e) => e.target.style.backgroundColor = ''}>Copy account number</button>
                        <button type="button" className="tooltip__btn-more" onMouseEnter={(e) => e.target.style.backgroundColor = '#e0e0e0'} onMouseLeave={(e) => e.target.style.backgroundColor = ''}>Edit Name and Color</button>
                    </div>
                    <a href="#" className="main-acc__act main-acc__act--money" onClick={(event) => event.preventDefault()}>
                        <span className="blind">Add money</span>
                    </a>
                    {account.progress !== undefined && account.progress !== null &&
                        <div className="main-acc__circle">
                            <svg className="graph-bar" width="100%" height="100%" viewBox="0 0 42 42">
                                <circle cx="21" cy="21" r="15.91549430918954" fill="transparent" stroke="rgba(0,0,0,0.07)" strokeWidth="1.5" />
                                <circle
                                className="gauge"
                                cx="21"
                                cy="21"
                                r="15.91549430918954"
                                fill="transparent"
                                stroke="#fff"
                                strokeWidth="1.5"
                                strokeLinecap="round"
                                strokeDashoffset="25"
                                style={{ strokeDasharray: `${account.progress > 100 ? 100 : account.progress} ${100 - (account.progress > 100 ? 100 : account.progress)}` }}
                                />
                            </svg>
                            <div className="main-acc__num">
                                <span className="percent">{account.progress}</span>
                                <span className="unit">%</span>
                            </div>
                        </div>
                    }
                </div>
            ))}

            {banners && banners.length > 0 && banners.map((banner, idx) => {
                const content = (
                    <div id={`banner-${idx}`}>
                        <span className="main-prod__cms-ico">
                            <img src={banner.image} alt="" />
                        </span>
                        <strong className="main-prod__tit">{banner.title}</strong>
                        <p className="main-prod__dsc">{banner.description}</p>
                    </div>
                );
                return banner.linkUrl ? (
                    <a href={banner.linkUrl} className="main-prod" rel="noopener noreferrer">
                        {content}
                    </a>
                ) : (
                    <div className="main-prod" aria-disabled="true" style={{ pointerEvents: 'none' }}>
                        {content}
                    </div>
                );
            })}

            {accounts && accounts.length > 0 &&
                <div className="main-tb">
                    <a href="#" className="link-to" onClick={(event) => event.preventDefault()}>
                    Total Balance
                    </a>
                </div>
            }
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
        <Dialog
            isOpen={withDrawDialog.isOpen}
            title={withDrawDialog.title}
            message={withDrawDialog.message}
            confirmLabel={withDrawDialog.confirmLabel}
            buttons={withDrawDialog.buttons}
            onClose={() => setWithDrawDialog(alertUtils.close())}
        />
        
        {snackbar.show && (
            <div style={{
                position: 'fixed',
                bottom: '20px',
                left: '50%',
                transform: 'translateX(-50%)',
                backgroundColor: '#323232',
                color: '#fff',
                padding: '14px 24px',
                borderRadius: '4px',
                boxShadow: '0 2px 5px rgba(0,0,0,0.3)',
                zIndex: 9999,
                fontSize: '14px',
                animation: 'slideUp 0.3s ease-out',
                minWidth: '250px',
                textAlign: 'center'
            }}>
                {snackbar.message}
            </div>
        )}
        </div>
    );
};

const mapStateToProps = (state) => {
    const { user } = state.user;
    const isLoading = state.banner.loading || state.account.loading || state.debitCard.loading || state.transaction.loading;
    return {
        user,
        isLoading,
        banners: state.banner.banners || [],
        accounts: state.account.accounts || [],
        debitCards: state.debitCard.debitCards || [],
        transactions: state.transaction.transactions || [],
    };
}

const mapDispatchToProps = (dispatch) => ({
    async deleteUserData() {
        dispatch({ type: 'USER_LOGOUT' });
    },
    async fetchBanners(userId) {
        dispatch({ type: 'BANNER_FETCH_REQUEST' });
        try {
            const { data } = await getBannerByUserIdApi(userId);
            dispatch({ type: 'BANNER_FETCH_SUCCESS', payload: data });
        } catch (error) {
            dispatch({ type: 'BANNER_FETCH_FAILURE', payload: error.message });
        }
    },
    async fetchAccounts(userId) {
        dispatch({ type: 'ACCOUNT_FETCH_REQUEST' });
        try {
            const { data } = await getAccountByUserIdApi(userId);
            dispatch({ type: 'ACCOUNT_FETCH_SUCCESS', payload: data });
        } catch (error) {
            dispatch({ type: 'ACCOUNT_FETCH_FAILURE', payload: error.message });
        }
    },
    async fetchDebitCards(userId) {
        dispatch({ type: 'DEBITCARD_FETCH_REQUEST' });
        try {
            const { data } = await getDebitCardByUserIdApi(userId);
            dispatch({ type: 'DEBITCARD_FETCH_SUCCESS', payload: data });
        } catch (error) {
            dispatch({ type: 'DEBITCARD_FETCH_FAILURE', payload: error.message });
        }
    },
    async fetchTransactions(userId, limit = 10, offset = 0) {
        dispatch({ type: 'TRANSACTION_FETCH_REQUEST' });
        try {
            const { data } = await getTransactionByUserIdApi(userId, limit, offset);
            dispatch({ type: 'TRANSACTION_FETCH_SUCCESS', payload: data });
        } catch (error) {
            dispatch({ type: 'TRANSACTION_FETCH_FAILURE', payload: error.message });
        }
    },
    async clearData() {
        dispatch({ type: 'TRANSACTION_CLEAR' });
        dispatch({ type: 'DEBITCARD_CLEAR' });
        dispatch({ type: 'ACCOUNT_CLEAR' });
        dispatch({ type: 'BANNER_CLEAR' });
    }
});

export default connect(mapStateToProps, mapDispatchToProps)(BankMain);
