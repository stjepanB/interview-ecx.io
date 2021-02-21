import React, { useState, useEffect } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';
import Button from '@material-ui/core/Button';
import { Modal, TextField, Grid } from '@material-ui/core';
import { getBooks, borrowBook } from '../api/booksApi';

const useStyles = makeStyles({
    table: {
        minWidth: 650,
    },
    head: {
        background: "darkBlue"
    }
});


export default function Books() {
    const classes = useStyles();
    const [open, setOpen] = useState(false);
    const [rows, setRows] = useState([])
    const [row, setRow] = useState(undefined);
    const [user, setUser] = useState(undefined);

    useEffect(() => {
        async function fetch() {
            const tmp = await getBooks();
            setRows(tmp);
        }
        fetch();
    }, [])

    const handleClose = () => {
        setOpen(false);
        setRow(undefined)
    }

    const handleOpen = (row) => {
        setOpen(true);
        setRow(row);
    }
    
    const createLoan = async function (e) {
        e.preventDefault();
        const loan = {
            "user" : user,
            "book_id": row.book_id
        }
        const response = await borrowBook(loan);

        if (response !== null) {
            console.log(response)
        } else {
            console.log("Response is NULL!")
        }

        window.location.reload();
    }

    return (
        <div>
            <h1>Catalog</h1>
            <TableContainer component={Paper}>
                <Table className={classes.table} aria-label="simple table">
                    <TableHead className={classes.head}>
                        <TableRow>
                            <TableCell>Title</TableCell>
                            <TableCell >Author</TableCell>
                            <TableCell >Genre</TableCell>
                            <TableCell >Price</TableCell>
                            <TableCell >Publish date</TableCell>
                            <TableCell >On loan</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {rows.map((row) => (
                            <TableRow key={row.book_id}>

                                <TableCell>{row.title}</TableCell>
                                <TableCell>{row.author}</TableCell>
                                <TableCell>{row.genre}</TableCell>
                                <TableCell>{row.price}</TableCell>
                                <TableCell>{row.publish_date}</TableCell>
                                <TableCell>{row.user ? row.user :  <Button variant="contained" onClick={() => handleOpen(row)}>Borrow</Button>}</TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
            <Modal
                open={open}
                onClose={handleClose}
                aria-labelledby="simple-modal-title"
                aria-describedby="simple-modal-description"

            >
                <Paper>
                    <h2 id="modal-title">
                        {row ? row.title : null}
                    </h2>
                    <form className={classes.root} onSubmit={createLoan} autoComplete="off">
                        <Grid container className={classes.root} spacing={2}>
                            <Grid item>
                                <TextField
                                    readonly
                                    multiline
                                    disabled
                                    placeholder={"Description"}
                                    rows={4}
                                    label={row? row.description : "none"}
                                />

                            </Grid>
                            <Grid item >
                                <TextField required id="outlined-basic"
                                    label="User name"
                                    variant="outlined"
                                    onChange={e => setUser(e.target.value)}
                                />
                            </Grid>
                            <Grid item>
                                <Button type="submit" primary> Borrow </Button>
                            </Grid>
                        </Grid>

                    </form>


                </Paper>

            </Modal>
        </div>
    );
}