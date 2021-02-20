import React, { useState, useEffect } from "react";
import { Grid, Paper, FormControlLabel, Radio, TableBody, RadioGroup, FormControl, TableContainer, Modal, TextField, Button, TableCell, Table, TableRow, TableHead } from '@material-ui/core';
import { makeStyles } from '@material-ui/core/styles';
import { getAvailableBooks } from "../api/booksApi"
import 'date-fns';
import DateFnsUtils from '@date-io/date-fns';
import {
  MuiPickersUtilsProvider,
  KeyboardDatePicker,
} from '@material-ui/pickers';

const useStyles = makeStyles({
    table: {
        minWidth: 650,
    },
    head: {
        background: "darkBlue"
    }
});
export default function Filter() {
    const classes = useStyles();
    const [rows, setRows] = useState([]);
    const [row, setRow] = useState(undefined);
    const [open, setOpen] = useState(false);
    const [selectedItem, setSelectedItem] = useState('start')
    const [query, setQuery] = useState("");

    useEffect(() => {
        async function fetch() {
            const tmp = await getAvailableBooks();
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

    const getQueryType = (type) => {

        if (type == 'publish_date') {
            return (
                <div>
                    <MuiPickersUtilsProvider utils={DateFnsUtils}>
                        <Grid container justify="space-around">
                            <KeyboardDatePicker
                                disableToolbar
                                variant="inline"
                                format="MM/dd/yyyy"
                                margin="normal"
                                id="date-picker-inline"
                                label="Date picker inline"
                                value={selectedDate}
                                onChange={handleDateChange}
                                KeyboardButtonProps={{
                                    'aria-label': 'change date',
                                }}
                            />
                        </Grid>
                    </MuiPickersUtilsProvider>
                </div>

            )
        }

        return (
            <div>
                <TextField label={selectedItem} />
                <Button color="primary"> Search</Button>
            </div>
        )
    }

    return (
        <div>
            <Paper>
                <Grid container>
                    <Grid item xs={12}>
                        <FormControl component="fieldset" size="medium" fullWidth>
                            <RadioGroup row aria-label='position' name='position'>
                                <FormControlLabel
                                    value="top"
                                    control={<Radio
                                        color="primary"
                                        checked={selectedItem === 'title'}
                                        onChange={(e) => { setSelectedItem("title") }}
                                    />}
                                    label="Title search"
                                    labelPlacement="start"
                                />
                                <FormControlLabel
                                    value="top"
                                    control={<Radio
                                        color="primary"
                                        checked={selectedItem === 'author'}
                                        onChange={(e) => { setSelectedItem("author") }}
                                    />}
                                    label="Author"
                                    labelPlacement="start"
                                />
                                <FormControlLabel
                                    value="top"
                                    control={<Radio
                                        color="primary"
                                        checked={selectedItem === 'description'}
                                        onChange={(e) => { setSelectedItem("description") }}
                                    />}
                                    label="Description"
                                    labelPlacement="start"
                                />

                                <FormControlLabel
                                    value="top"
                                    control={<Radio
                                        color="primary"
                                        checked={selectedItem === 'publish_date'}
                                        onChange={(e) => { setSelectedItem("publish_date") }}
                                    />}
                                    label="Publish date"
                                    labelPlacement="start"
                                />
                            </RadioGroup>
                        </FormControl>
                    </Grid>
                    <Grid item xs={8}>
                        {getQueryType(selectedItem)}
                    </Grid>
                    <Grid item xs={12}>
                        <h1>Results</h1>
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
                                            <TableCell>{row.user ? row.user : <Button contained onClick={() => handleOpen(row)}>Borrow</Button>}</TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                        </TableContainer>
                    </Grid>
                </Grid>

            </Paper>
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
                                    label={row ? row.description : "none"}
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
    )
}