# MedTracker: Changeblog

## Dec 28, 2022
I took the last week or so away from this in order to focus on the holidays. I've forgotten somewhat where I was. \
I think its best if I write a short "Up next preview to keep my head on straight"

### Up next: 
Create alarm based on time, that queries "Meds with this time" and adds each to the list of meds needed
```
Notification: 7:00 AM
Message:
"Don't forget to take your meds!"
MED: Example-eral <Checkbox>
MED: Test-osterone <Checkbox>
MED: Drug name <Checkbox>

Buttons: 
Cancel -> Marks all as missed/cancelled
Postpone for <TIME> -> creates a new notif from this notif pushed by TIME add "Postponed from <THIS-TIME>" to the start of the new message 
Partial complete -> Open app with info from this notif to mark which meds taken and which not
Took all my meds -> Mark meds as taken, clear notif
```

Make Icon. Something like a pill & an exclamation point.