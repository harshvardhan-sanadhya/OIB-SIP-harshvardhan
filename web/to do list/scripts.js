function saveTask(){
    const title=document.getElementById("title").value;
    const description = document.getElementById("description").value;
    if(title.trim()===''||description.trim()===''){
        alert('Please Enter both title and description');
        return;
    }
    const taskRow=document.createElement('div');
    taskRow.className="task-row";

    const contentdiv=document.createElement('div');
    contentdiv.className="task-content";

    const tasktitle=document.createElement('h3');
    tasktitle.textContent=title;
    tasktitle.className="t1";

    const taskdes=document.createElement('div');
    taskdes.textContent=description;
    taskdes.className="t2";
    
    contentdiv.appendChild(tasktitle);
    contentdiv.appendChild(taskdes);

    const deletebtn=document.createElement('button');
    deletebtn.textContent="X";
    deletebtn.className="delete-btn";
    deletebtn.onclick=function(){
        taskRow.remove();
    };
    taskRow.appendChild(tasktitle)
    taskRow.appendChild(taskdes)
    taskRow.appendChild(deletebtn);



document.getElementById('output').appendChild(taskRow);

document.getElementById("title").value='';
document.getElementById("description").value='';

};