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
import { getBooks } from '../api/booksApi';

const useStyles = makeStyles({
    table: {
        minWidth: 650,
    },
    head: {
        background: "darkBlue"
    }
});
function createData(id, title, author, genre, price, publish_date, user) {
    return { id, title, author, genre, price, publish_date, user };
}

const rows = [
    createData('bk107', 'Splish Splash', 'Thurman, Paula', 'Romance', '4.95', '2000-11-02'),
    createData('bk106', 'Lover Birds', 'Randall, Cynthia', 'Romance', '4.95', '2000-09-02', 'Miroslav Joškić')
];

export default function Books() {
    const classes = useStyles();
    const [open, setOpen] = useState(false);
    const [rows, setRows] = useState([])
    const [row, setRow] = useState(undefined)

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
                    <form className={classes.root} autoComplete="off">
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
                                <TextField required id="outlined-basic" label="User name" variant="outlined" />
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